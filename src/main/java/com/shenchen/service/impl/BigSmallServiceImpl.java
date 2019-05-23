package com.shenchen.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.shenchen.dao.IBigSmallDao;
import com.shenchen.dao.IGameBaseDao;
import com.shenchen.model.*;
import com.shenchen.service.IBigSmallService;
import com.shenchen.util.RedisPoolJava;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("bigSmallService")
public class BigSmallServiceImpl implements IBigSmallService {

    protected final static Logger logger = Logger.getLogger(BigSmallServiceImpl.class);

    @Autowired
    IBigSmallDao bigSmallDao;

    @Autowired
    IGameBaseDao gameBaseDao;

    @Override
    public void analyseBigSmall() {
        bigSmallDao.updateAllBigSmallData();
        //获取大小球数据
        List<BigSmallData> bigSmallDataList = bigSmallDao.getBigSmallData();
        for(BigSmallData bigSmallData :  bigSmallDataList){
            //获取主队历史比赛数据
            GameBaseData queryGameBaseData = new GameBaseData();
            queryGameBaseData.setLeague_name_simply(bigSmallData.getLeague_name_simply());
            queryGameBaseData.setMatch_time(bigSmallData.getMatch_time());
            queryGameBaseData.setHost_name(bigSmallData.getHost_name());
            queryGameBaseData.setGuest_name(bigSmallData.getGuest_name());
            List<GameBaseData> hostGameBaseData = gameBaseDao.queryHostGameBaseData(queryGameBaseData);
            if(CollectionUtils.isEmpty(hostGameBaseData) || hostGameBaseData.size() < 10){
                continue;
            }
            //主队
            HostGoalData hostGoalData = new HostGoalData();
            hostGoalData.setLeague_name_simply(bigSmallData.getLeague_name_simply());
            hostGoalData.setTeam_name(bigSmallData.getHost_name());
            hostGoalData.setGameNumber(new BigDecimal(hostGameBaseData.size()));
            for(GameBaseData gameBaseData : hostGameBaseData){
                hostGoalData.setGetGoal(hostGoalData.getGetGoal().add(new BigDecimal(gameBaseData.getHost_goal())));
                hostGoalData.setLostGoal(hostGoalData.getLostGoal().add(new BigDecimal(gameBaseData.getGuest_goal())));
            }
            //客队
            List<GameBaseData> guestGameBaseData = gameBaseDao.queryGuestGameBaseData(queryGameBaseData);
            if(CollectionUtils.isEmpty(guestGameBaseData) || guestGameBaseData.size() < 10){
                continue;
            }
            GuestGoalData guestGoalData = new GuestGoalData();
            guestGoalData.setLeague_name_simply(bigSmallData.getLeague_name_simply());
            guestGoalData.setTeam_name(bigSmallData.getGuest_name());
            guestGoalData.setGameNumber(new BigDecimal(guestGameBaseData.size()));
            for(GameBaseData gameBaseData : guestGameBaseData){
                guestGoalData.setGetGoal(guestGoalData.getGetGoal().add(new BigDecimal(gameBaseData.getGuest_goal())));
                guestGoalData.setLostGoal(guestGoalData.getLostGoal().add(new BigDecimal(gameBaseData.getHost_goal())));
            }

            BigDecimal perCount;

            BigDecimal hostGetGoal = hostGoalData.getGetGoal().divide(hostGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
            hostGetGoal = hostGetGoal.multiply(new BigDecimal(0.55));
            BigDecimal hostLostGoal = hostGoalData.getLostGoal().divide(hostGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
            hostLostGoal = hostLostGoal.multiply(new BigDecimal(0.45));

            BigDecimal guestGetGoal = guestGoalData.getGetGoal().divide(guestGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
            guestGetGoal = guestGetGoal.multiply(new BigDecimal(0.45));
            BigDecimal guestLostGoal = hostGoalData.getLostGoal().divide(guestGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
            guestLostGoal = guestLostGoal.multiply(new BigDecimal(0.55));


            perCount = hostGetGoal.add(hostLostGoal).add(guestGetGoal).add(guestLostGoal);

            bigSmallData.setBuy_result(new BigDecimal(0));
            if(bigSmallData.getFirst_let_big_small().doubleValue() > perCount.doubleValue()){

                if(bigSmallData.getFirst_big().doubleValue() >= bigSmallData.getFirst_small().doubleValue()){
                    bigSmallData.setBig_small_pre(2);
                    bigSmallData.setBuy_result(bigSmallData.getBuy_small());
                }else {
                    continue;
                }

            }else if (bigSmallData.getFirst_let_big_small().doubleValue() < perCount.doubleValue()){

                if(bigSmallData.getFirst_big().doubleValue() <= bigSmallData.getFirst_small().doubleValue()){
                    bigSmallData.setBig_small_pre(1);
                    bigSmallData.setBuy_result(bigSmallData.getBuy_big());
                }else {
                    continue;
                }
            }else{
                continue;
            }
            bigSmallData.setBuy_result(bigSmallData.getBuy_result().subtract(new BigDecimal(1)));
            bigSmallDao.updateBigSmallData(bigSmallData);


        }

    }

    @Override
    public void analyseBigSmall(String league_name_simply, boolean reverse,double hostGet, double hostLost, double guestGet, double guestLost) {

        List<Map<String, Object>> returnMap = new ArrayList<>();
        bigSmallDao.updateBigSmallDataByLeague(league_name_simply);
        //获取大小球数据
        List<BigSmallData> bigSmallDataList = bigSmallDao.getBigSmallData();
        for(BigSmallData bigSmallData :  bigSmallDataList){
            if(!league_name_simply.equals(bigSmallData.getLeague_name_simply())){
                continue;
            }
            //获取主队历史比赛数据
            GameBaseData queryGameBaseData = new GameBaseData();
            queryGameBaseData.setLeague_name_simply(bigSmallData.getLeague_name_simply());
            queryGameBaseData.setMatch_time(bigSmallData.getMatch_time());
            queryGameBaseData.setHost_name(bigSmallData.getHost_name());
            queryGameBaseData.setGuest_name(bigSmallData.getGuest_name());
            List<GameBaseData> hostGameBaseData = gameBaseDao.queryHostGameBaseData(queryGameBaseData);
            if(CollectionUtils.isEmpty(hostGameBaseData) || hostGameBaseData.size() < 10){
                continue;
            }
            //主队
            HostGoalData hostGoalData = new HostGoalData();
            hostGoalData.setLeague_name_simply(bigSmallData.getLeague_name_simply());
            hostGoalData.setTeam_name(bigSmallData.getHost_name());
            hostGoalData.setGameNumber(new BigDecimal(hostGameBaseData.size()));
            for(GameBaseData gameBaseData : hostGameBaseData){
                hostGoalData.setGetGoal(hostGoalData.getGetGoal().add(new BigDecimal(gameBaseData.getHost_goal())));
                hostGoalData.setLostGoal(hostGoalData.getLostGoal().add(new BigDecimal(gameBaseData.getGuest_goal())));
            }
            //客队
            List<GameBaseData> guestGameBaseData = gameBaseDao.queryGuestGameBaseData(queryGameBaseData);
            if(CollectionUtils.isEmpty(guestGameBaseData) || guestGameBaseData.size() < 10){
                continue;
            }
            GuestGoalData guestGoalData = new GuestGoalData();
            guestGoalData.setLeague_name_simply(bigSmallData.getLeague_name_simply());
            guestGoalData.setTeam_name(bigSmallData.getGuest_name());
            guestGoalData.setGameNumber(new BigDecimal(guestGameBaseData.size()));
            for(GameBaseData gameBaseData : guestGameBaseData){
                guestGoalData.setGetGoal(guestGoalData.getGetGoal().add(new BigDecimal(gameBaseData.getGuest_goal())));
                guestGoalData.setLostGoal(guestGoalData.getLostGoal().add(new BigDecimal(gameBaseData.getHost_goal())));
            }

            BigDecimal perCount;

            BigDecimal hostGetGoal = hostGoalData.getGetGoal().divide(hostGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
            hostGetGoal = hostGetGoal.multiply(new BigDecimal(hostGet));
            BigDecimal hostLostGoal = hostGoalData.getLostGoal().divide(hostGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
            hostLostGoal = hostLostGoal.multiply(new BigDecimal(hostLost));

            BigDecimal guestGetGoal = guestGoalData.getGetGoal().divide(guestGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
            guestGetGoal = guestGetGoal.multiply(new BigDecimal(guestGet));
            BigDecimal guestLostGoal = hostGoalData.getLostGoal().divide(guestGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
            guestLostGoal = guestLostGoal.multiply(new BigDecimal(guestLost));

            perCount = hostGetGoal.add(hostLostGoal).add(guestGetGoal).add(guestLostGoal);

            bigSmallData.setBuy_result(new BigDecimal(0));

            if(reverse){
                if(bigSmallData.getFirst_let_big_small().doubleValue() > perCount.doubleValue()){

                    if(bigSmallData.getFirst_big().doubleValue() >= bigSmallData.getFirst_small().doubleValue()){
                        bigSmallData.setBig_small_pre(1);
                        bigSmallData.setBuy_result(bigSmallData.getBuy_big());
                    }else {
                        continue;
                    }
                }else if (bigSmallData.getFirst_let_big_small().doubleValue() < perCount.doubleValue()){

                    if(bigSmallData.getFirst_big().doubleValue() <= bigSmallData.getFirst_small().doubleValue()){
                        bigSmallData.setBig_small_pre(2);
                        bigSmallData.setBuy_result(bigSmallData.getBuy_small());

                    }else {
                        continue;
                    }
                }else{
                    continue;
                }

            }else{
                if(bigSmallData.getFirst_let_big_small().doubleValue() > perCount.doubleValue()){

                    if(bigSmallData.getFirst_big().doubleValue() >= bigSmallData.getFirst_small().doubleValue()){
                        bigSmallData.setBig_small_pre(2);
                        bigSmallData.setBuy_result(bigSmallData.getBuy_small());
                    }else {
                        continue;
                    }
                }else if (bigSmallData.getFirst_let_big_small().doubleValue() < perCount.doubleValue()){

                    if(bigSmallData.getFirst_big().doubleValue() <= bigSmallData.getFirst_small().doubleValue()){
                        bigSmallData.setBig_small_pre(1);
                        bigSmallData.setBuy_result(bigSmallData.getBuy_big());
                    }else {
                        continue;
                    }
                }else{
                    continue;
                }
            }





            bigSmallData.setBuy_result(bigSmallData.getBuy_result().subtract(new BigDecimal(1)));
            bigSmallDao.updateBigSmallData(bigSmallData);
        }

    }

    @Override
    public List<Map<String, Object>> analyseBigSmall(String league_name_simply) {
        List<Map<String, Object>> returnMap = new ArrayList<>();
        bigSmallDao.updateBigSmallDataByLeague(league_name_simply);
        //获取大小球数据
        List<BigSmallData> bigSmallDataList = bigSmallDao.getBigSmallData();
        double hostGet = 1D;double hostLost = 0D;
        while(hostGet > 0){
            double guestGet= 1D;double guestLost = 0D;
            while(guestGet > 0){
                List<BigSmallData> updateBigSmallData = new ArrayList<>();
                for(BigSmallData bigSmallData :  bigSmallDataList){
                    if(!league_name_simply.equals(bigSmallData.getLeague_name_simply())){
                        continue;
                    }
                    //获取主队历史比赛数据
                    GameBaseData queryGameBaseData = new GameBaseData();
                    queryGameBaseData.setLeague_name_simply(bigSmallData.getLeague_name_simply());
                    queryGameBaseData.setMatch_time(bigSmallData.getMatch_time());
                    queryGameBaseData.setHost_name(bigSmallData.getHost_name());
                    queryGameBaseData.setGuest_name(bigSmallData.getGuest_name());
                    List<GameBaseData> hostGameBaseData = gameBaseDao.queryHostGameBaseData(queryGameBaseData);
                    if(CollectionUtils.isEmpty(hostGameBaseData) || hostGameBaseData.size() < 10){
                        continue;
                    }
                    //主队
                    HostGoalData hostGoalData = new HostGoalData();
                    hostGoalData.setLeague_name_simply(bigSmallData.getLeague_name_simply());
                    hostGoalData.setTeam_name(bigSmallData.getHost_name());
                    hostGoalData.setGameNumber(new BigDecimal(hostGameBaseData.size()));
                    for(GameBaseData gameBaseData : hostGameBaseData){
                        hostGoalData.setGetGoal(hostGoalData.getGetGoal().add(new BigDecimal(gameBaseData.getHost_goal())));
                        hostGoalData.setLostGoal(hostGoalData.getLostGoal().add(new BigDecimal(gameBaseData.getGuest_goal())));
                    }
                    //客队
                    List<GameBaseData> guestGameBaseData = gameBaseDao.queryGuestGameBaseData(queryGameBaseData);
                    if(CollectionUtils.isEmpty(guestGameBaseData) || guestGameBaseData.size() < 10){
                        continue;
                    }
                    GuestGoalData guestGoalData = new GuestGoalData();
                    guestGoalData.setLeague_name_simply(bigSmallData.getLeague_name_simply());
                    guestGoalData.setTeam_name(bigSmallData.getGuest_name());
                    guestGoalData.setGameNumber(new BigDecimal(guestGameBaseData.size()));
                    for(GameBaseData gameBaseData : guestGameBaseData){
                        guestGoalData.setGetGoal(guestGoalData.getGetGoal().add(new BigDecimal(gameBaseData.getGuest_goal())));
                        guestGoalData.setLostGoal(guestGoalData.getLostGoal().add(new BigDecimal(gameBaseData.getHost_goal())));
                    }

                    BigDecimal perCount;

                    BigDecimal hostGetGoal = hostGoalData.getGetGoal().divide(hostGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
                    hostGetGoal = hostGetGoal.multiply(new BigDecimal(hostGet));
                    BigDecimal hostLostGoal = hostGoalData.getLostGoal().divide(hostGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
                    hostLostGoal = hostLostGoal.multiply(new BigDecimal(hostLost));

                    BigDecimal guestGetGoal = guestGoalData.getGetGoal().divide(guestGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
                    guestGetGoal = guestGetGoal.multiply(new BigDecimal(guestGet));
                    BigDecimal guestLostGoal = hostGoalData.getLostGoal().divide(guestGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
                    guestLostGoal = guestLostGoal.multiply(new BigDecimal(guestLost));

                    perCount = hostGetGoal.add(hostLostGoal).add(guestGetGoal).add(guestLostGoal);

                    bigSmallData.setBuy_result(new BigDecimal(0));
                    if(bigSmallData.getFirst_let_big_small().doubleValue() > perCount.doubleValue()){

                        if(bigSmallData.getFirst_big().doubleValue() >= bigSmallData.getFirst_small().doubleValue()){
                            bigSmallData.setBig_small_pre(2);
                            bigSmallData.setBuy_result(bigSmallData.getBuy_small());
                        }else {
                            continue;
                        }
                    }else if (bigSmallData.getFirst_let_big_small().doubleValue() < perCount.doubleValue()){

                        if(bigSmallData.getFirst_big().doubleValue() <= bigSmallData.getFirst_small().doubleValue()){
                            bigSmallData.setBig_small_pre(1);
                            bigSmallData.setBuy_result(bigSmallData.getBuy_big());
                        }else {
                            continue;
                        }
                    }else{
                        continue;
                    }
                    bigSmallData.setBuy_result(bigSmallData.getBuy_result().subtract(new BigDecimal(1)));
                    bigSmallDao.updateBigSmallData(bigSmallData);
                    updateBigSmallData.add(bigSmallData);

                }
                Map<String, Object> map = new HashMap<>();
                map.put("league_name_simply",league_name_simply);
                map.put("hostGet",hostGet);
                map.put("hostLost",hostLost);
                map.put("guestGet",guestGet);
                map.put("guestLost",guestLost);
                map.put("totalCount",updateBigSmallData.size());
                Integer winCount = 0;
                Integer lostCount = 0;
                double sum = 0D;
                for(BigSmallData bigSmallData : updateBigSmallData){
                    sum = sum + bigSmallData.getBuy_result().doubleValue();
                    if(bigSmallData.getBuy_result().doubleValue() > 0 ){
                        winCount = winCount + 1;
                    }else if (bigSmallData.getBuy_result().doubleValue() < 0){
                        lostCount = lostCount + 1;
                    }
                }
                map.put("sum",sum);
                map.put("winCount",winCount);
                map.put("lostCount",lostCount);
                map.put("rate",sum/updateBigSmallData.size());
                returnMap.add(map);
                guestGet = guestGet - 0.05D;
                guestLost = guestLost + 0.05D;
            }
            hostGet = hostGet - 0.05D;
            hostLost =hostLost + 0.05D;
        }
        return returnMap;

    }


    @Override
    public List<BigSmallResultData> analyseBigSmallEfficient(String league_name_simply, boolean reverse) {
        List<BigSmallResultData> returnMap = new ArrayList<>();
        //获取大小球数据
        List<BigSmallData> bigSmallDataList = bigSmallDao.getBigSmallData();
        double hostGet = 1D;double hostLost = 0D;
        while(hostGet > 0){
            double guestGet= 1D;double guestLost = 0D;
            while(guestGet > 0){
                List<BigSmallData> updateBigSmallData = new ArrayList<>();
                for(BigSmallData bigSmallData :  bigSmallDataList){
                    if(!league_name_simply.equals(bigSmallData.getLeague_name_simply())){
                        continue;
                    }
                    //获取主队历史比赛数据
                    HostGoalData hostGoalData = this.getHostGoalData(bigSmallData);
                    if(hostGoalData == null){
                        continue;
                    }
                    GuestGoalData guestGoalData = this.getGuestGoalData(bigSmallData);
                    if(guestGoalData == null){
                        continue;
                    }
                    BigDecimal perCount;
                    BigDecimal hostGetGoal = hostGoalData.getGetGoal().divide(hostGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
                    hostGetGoal = hostGetGoal.multiply(new BigDecimal(hostGet));
                    BigDecimal hostLostGoal = hostGoalData.getLostGoal().divide(hostGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
                    hostLostGoal = hostLostGoal.multiply(new BigDecimal(hostLost));

                    BigDecimal guestGetGoal = guestGoalData.getGetGoal().divide(guestGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
                    guestGetGoal = guestGetGoal.multiply(new BigDecimal(guestGet));
                    BigDecimal guestLostGoal = hostGoalData.getLostGoal().divide(guestGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
                    guestLostGoal = guestLostGoal.multiply(new BigDecimal(guestLost));

                    perCount = hostGetGoal.add(hostLostGoal).add(guestGetGoal).add(guestLostGoal);

                    bigSmallData.setBuy_result(new BigDecimal(0));

                    if(bigSmallData.getFirst_let_big_small().doubleValue() > perCount.doubleValue()){

                        if(bigSmallData.getFirst_big().doubleValue() >= bigSmallData.getFirst_small().doubleValue()){
                            bigSmallData.setBig_small_pre(2);
                            bigSmallData.setBuy_result(bigSmallData.getBuy_small());
                        }else {
                            continue;
                        }
                    }else if (bigSmallData.getFirst_let_big_small().doubleValue() < perCount.doubleValue()){

                        if(bigSmallData.getFirst_big().doubleValue() <= bigSmallData.getFirst_small().doubleValue()){
                            bigSmallData.setBig_small_pre(1);
                            bigSmallData.setBuy_result(bigSmallData.getBuy_big());
                        }else {
                            continue;
                        }
                    }else{
                        continue;
                    }


                    if(reverse){
                        if(bigSmallData.getBig_small_pre() == 1){
                            bigSmallData.setBig_small_pre(2);
                            bigSmallData.setBuy_result(bigSmallData.getBuy_small());
                        }else{
                            bigSmallData.setBig_small_pre(1);
                            bigSmallData.setBuy_result(bigSmallData.getBuy_big());
                        }

                    }
                    bigSmallData.setBuy_result(bigSmallData.getBuy_result().subtract(new BigDecimal(1)));
                    BigSmallData toAddBig = new BigSmallData();
                    BeanUtils.copyProperties(bigSmallData,toAddBig);
                    updateBigSmallData.add(toAddBig);

                }

                if(!CollectionUtils.isEmpty(updateBigSmallData)){
                    BigSmallResultData bigSmallResultData = new BigSmallResultData();
                    bigSmallResultData.setLeague_name_simply(league_name_simply);
                    bigSmallResultData.setHostGet(hostGet);
                    bigSmallResultData.setHostLost(hostLost);

                    bigSmallResultData.setGuestGet(guestGet);
                    bigSmallResultData.setGuestLost(guestLost);
                    bigSmallResultData.setTotalCount(updateBigSmallData.size());


                    Integer winCount = 0;
                    Integer lostCount = 0;
                    double sum = 0D;
                    for(BigSmallData bigSmallData : updateBigSmallData){
                        sum = sum + bigSmallData.getBuy_result().doubleValue();
                        if(bigSmallData.getBuy_result().doubleValue() > 0 ){
                            winCount = winCount + 1;
                        }else if (bigSmallData.getBuy_result().doubleValue() < 0){
                            lostCount = lostCount + 1;
                        }
                    }
                    bigSmallResultData.setSum(sum);
                    bigSmallResultData.setWinCount(winCount);
                    bigSmallResultData.setLostCount(lostCount);
                    bigSmallResultData.setRate(sum/updateBigSmallData.size());
                    BigDecimal winRate = new BigDecimal(winCount).divide(new BigDecimal(updateBigSmallData.size()),5,BigDecimal.ROUND_HALF_UP);
                    bigSmallResultData.setWinRate(winRate.doubleValue());
                    returnMap.add(bigSmallResultData);
                }
                guestGet = guestGet - 0.05D;
                guestLost = guestLost + 0.05D;
            }
            hostGet = hostGet - 0.05D;
            hostLost =hostLost + 0.05D;
        }
        return returnMap;

    }


    /**
     * 主队进球数据
     * @param bigSmallData
     * @return
     */
    private HostGoalData getHostGoalData(BigSmallData bigSmallData){
        //主队
        HostGoalData hostGoalData = new HostGoalData();
        //redis 获取 matchId+host
        String hostJson = RedisPoolJava.getValue(bigSmallData.getMatch_id()+ "_host_goal");
        logger.info("getHostGoalData redis data :"+hostJson);
        if(StringUtils.isNotBlank(hostJson)){
            if("skip".equals(hostJson)){
                return null;
            }
            hostGoalData = JSON.parseObject(hostJson, new TypeReference<HostGoalData>(){});
            return hostGoalData;
        }
        //获取主队历史比赛数据
        GameBaseData queryGameBaseData = new GameBaseData();
        queryGameBaseData.setLeague_name_simply(bigSmallData.getLeague_name_simply());
        queryGameBaseData.setMatch_time(bigSmallData.getMatch_time());
        queryGameBaseData.setHost_name(bigSmallData.getHost_name());
        queryGameBaseData.setGuest_name(bigSmallData.getGuest_name());
        List<GameBaseData> hostGameBaseData = gameBaseDao.queryHostGameBaseData(queryGameBaseData);
        if(CollectionUtils.isEmpty(hostGameBaseData) || hostGameBaseData.size() < 10){
            RedisPoolJava.setValue(bigSmallData.getMatch_id()+ "_host_goal","skip");
            return null;
        }
        hostGoalData.setLeague_name_simply(bigSmallData.getLeague_name_simply());
        hostGoalData.setTeam_name(bigSmallData.getHost_name());
        hostGoalData.setGameNumber(new BigDecimal(hostGameBaseData.size()));
        for(GameBaseData gameBaseData : hostGameBaseData){
            hostGoalData.setGetGoal(hostGoalData.getGetGoal().add(new BigDecimal(gameBaseData.getHost_goal())));
            hostGoalData.setLostGoal(hostGoalData.getLostGoal().add(new BigDecimal(gameBaseData.getGuest_goal())));
        }
        RedisPoolJava.setValue(bigSmallData.getMatch_id()+ "_host_goal",JSON.toJSONString(hostGoalData));
        return hostGoalData;
    }


    /**
     * 获取客场历史进球数据
     * @param bigSmallData
     * @return
     */
    private GuestGoalData getGuestGoalData(BigSmallData bigSmallData){
        //客队
        GuestGoalData guestGoalData = new GuestGoalData();
        //redis 获取 matchId+host
        String guestJson = RedisPoolJava.getValue(bigSmallData.getMatch_id()+ "_guest_goal");
        logger.info("getGuestGoalData redis data :" + guestJson);
        if(StringUtils.isNotBlank(guestJson)){
            if("skip".equals(guestJson)){
                return null;
            }
            guestGoalData = JSON.parseObject(guestJson, new TypeReference<GuestGoalData>(){});
            return guestGoalData;
        }
        //获取客队历史比赛数据
        GameBaseData queryGameBaseData = new GameBaseData();
        queryGameBaseData.setLeague_name_simply(bigSmallData.getLeague_name_simply());
        queryGameBaseData.setMatch_time(bigSmallData.getMatch_time());
        queryGameBaseData.setHost_name(bigSmallData.getHost_name());
        queryGameBaseData.setGuest_name(bigSmallData.getGuest_name());
        //客队
        List<GameBaseData> guestGameBaseData = gameBaseDao.queryGuestGameBaseData(queryGameBaseData);
        if(CollectionUtils.isEmpty(guestGameBaseData) || guestGameBaseData.size() < 10){
            RedisPoolJava.setValue(bigSmallData.getMatch_id()+ "_guest_goal","skip");
            return null;
        }
        guestGoalData.setLeague_name_simply(bigSmallData.getLeague_name_simply());
        guestGoalData.setTeam_name(bigSmallData.getGuest_name());
        guestGoalData.setGameNumber(new BigDecimal(guestGameBaseData.size()));
        for(GameBaseData gameBaseData : guestGameBaseData){
            guestGoalData.setGetGoal(guestGoalData.getGetGoal().add(new BigDecimal(gameBaseData.getGuest_goal())));
            guestGoalData.setLostGoal(guestGoalData.getLostGoal().add(new BigDecimal(gameBaseData.getHost_goal())));
        }
        RedisPoolJava.setValue(bigSmallData.getMatch_id()+ "_guest_goal",JSON.toJSONString(guestGoalData));
        return guestGoalData;


    }


}
