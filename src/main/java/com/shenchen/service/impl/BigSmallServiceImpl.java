package com.shenchen.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.shenchen.dao.IBigSmallDao;
import com.shenchen.dao.IBigSmallModulusDao;
import com.shenchen.dao.IGameBaseDao;
import com.shenchen.model.*;
import com.shenchen.service.IBigSmallService;
import com.shenchen.util.DateUtils;
import com.shenchen.util.RedisPoolJava;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service("bigSmallService")
public class BigSmallServiceImpl implements IBigSmallService {

    protected final static Logger logger = Logger.getLogger(BigSmallServiceImpl.class);

    @Autowired
    IBigSmallDao bigSmallDao;

    @Autowired
    IGameBaseDao gameBaseDao;

    @Autowired
    IBigSmallModulusDao bigSmallModulusDao;

    @Override
    public List<BigSmallModulus> analyseBigSmallEfficient(String league_name_simply,String company, boolean reverse) {
        List<BigSmallModulus> returnMap = new ArrayList<>();
        //获取大小球数据
        BigSmallData queryBigSmallData = new BigSmallData();
        queryBigSmallData.setLeague_name_simply(league_name_simply);
        queryBigSmallData.setCompany_name(company);
        List<BigSmallData> bigSmallDataList = bigSmallDao.getBigSmallHistoryDataBy(queryBigSmallData);
        BigDecimal hostGet = new BigDecimal(1);
        BigDecimal hostLost = new BigDecimal(0);
        while(hostGet.doubleValue() > 0){
            BigDecimal guestGet= new BigDecimal(1);
            BigDecimal guestLost = new BigDecimal(0);
            while(guestGet.doubleValue() > 0){
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
                    BigDecimal hostGetGoal = hostGoalData.getGetGoal().divide(hostGoalData.getGameNumber(),2,BigDecimal.ROUND_HALF_UP);
                    hostGetGoal = hostGetGoal.multiply(hostGet);
                    BigDecimal hostLostGoal = hostGoalData.getLostGoal().divide(hostGoalData.getGameNumber(),2,BigDecimal.ROUND_HALF_UP);
                    hostLostGoal = hostLostGoal.multiply(hostLost);

                    BigDecimal guestGetGoal = guestGoalData.getGetGoal().divide(guestGoalData.getGameNumber(),2,BigDecimal.ROUND_HALF_UP);
                    guestGetGoal = guestGetGoal.multiply(guestGet);
                    BigDecimal guestLostGoal = hostGoalData.getLostGoal().divide(guestGoalData.getGameNumber(),2,BigDecimal.ROUND_HALF_UP);
                    guestLostGoal = guestLostGoal.multiply(guestLost);

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
                    BigSmallModulus bigSmallModulus = new BigSmallModulus();
                    bigSmallModulus.setCompany_name(company);
                    bigSmallModulus.setLeague_name_simply(league_name_simply);
                    bigSmallModulus.setHost_get(hostGet);
                    bigSmallModulus.setHost_lost(hostLost);

                    bigSmallModulus.setGuest_get(guestGet);
                    bigSmallModulus.setGuest_lost(guestLost);
                    bigSmallModulus.setTotal_count(updateBigSmallData.size());


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
                    if(winCount + lostCount > 0 ){
                        bigSmallModulus.setSum(new BigDecimal(sum));
                        bigSmallModulus.setWin_count(winCount);
                        bigSmallModulus.setLost_count(lostCount);
                        bigSmallModulus.setBuy_count(winCount + lostCount);
                        bigSmallModulus.setRate(new BigDecimal(sum/bigSmallModulus.getBuy_count()));
                        BigDecimal winRate = new BigDecimal(winCount).divide(new BigDecimal(bigSmallModulus.getBuy_count()),5,BigDecimal.ROUND_HALF_UP);
                        bigSmallModulus.setWin_rate(winRate);
                        bigSmallModulus.setReverse(reverse ? 2 : 1);
                        returnMap.add(bigSmallModulus);
                    }

                }
                guestGet = guestGet.subtract(new BigDecimal(0.05));
                guestLost = guestLost.add(new BigDecimal(0.05));
            }
            hostGet = hostGet.subtract(new BigDecimal(0.05));
            hostLost =hostLost.add(new BigDecimal(0.05));
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

    @Override
    public void calculationResult(){
        //获取未开赛的比赛
        List<BigSmallData> newBigSmallData = bigSmallDao.getBigSmallData();
        for(BigSmallData bigSmallData : newBigSmallData){
            if(bigSmallData.getModulus_id() != null){
                continue;
            }
            if(bigSmallData.getIs_end() == 1){
                continue;
            }
//            if(bigSmallData.getMatch_time().getTime() - 60 * 60000> new Date().getTime()){
                BigSmallModulus query = new BigSmallModulus();
                query.setLeague_name_simply(bigSmallData.getLeague_name_simply());
                query.setCompany_name(bigSmallData.getCompany_name());
                BigSmallModulus result = bigSmallModulusDao.getOneBigSmallModulus(query);
                if(result != null){
                    if(result.getWin_rate().doubleValue() < 0.56 || result.getRate().doubleValue() < 0){
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
                    BigDecimal hostGetGoal = hostGoalData.getGetGoal().divide(hostGoalData.getGameNumber(),2,BigDecimal.ROUND_HALF_UP);
                    hostGetGoal = hostGetGoal.multiply(result.getHost_get());
                    BigDecimal hostLostGoal = hostGoalData.getLostGoal().divide(hostGoalData.getGameNumber(),2,BigDecimal.ROUND_HALF_UP);
                    hostLostGoal = hostLostGoal.multiply(result.getHost_lost());

                    BigDecimal guestGetGoal = guestGoalData.getGetGoal().divide(guestGoalData.getGameNumber(),2,BigDecimal.ROUND_HALF_UP);
                    guestGetGoal = guestGetGoal.multiply(result.getGuest_get());
                    BigDecimal guestLostGoal = hostGoalData.getLostGoal().divide(guestGoalData.getGameNumber(),2,BigDecimal.ROUND_HALF_UP);
                    guestLostGoal = guestLostGoal.multiply(result.getGuest_lost());
                    perCount = hostGetGoal.add(hostLostGoal).add(guestGetGoal).add(guestLostGoal);
                    if(bigSmallData.getFirst_let_big_small().doubleValue() > perCount.doubleValue()){
                        if(bigSmallData.getFirst_big().doubleValue() >= bigSmallData.getFirst_small().doubleValue()){
                            bigSmallData.setBig_small_pre(2);
                        }else {
                            continue;
                        }
                    }else if (bigSmallData.getFirst_let_big_small().doubleValue() < perCount.doubleValue()){
                        if(bigSmallData.getFirst_big().doubleValue() <= bigSmallData.getFirst_small().doubleValue()){
                            bigSmallData.setBig_small_pre(1);
                        }else {
                            continue;
                        }
                    }else{
                        continue;
                    }
                    if(result.getReverse() == 2){
                        if(bigSmallData.getBig_small_pre() == 1){
                            bigSmallData.setBig_small_pre(2);
                        }else{
                            bigSmallData.setBig_small_pre(1);
                        }
                    }
                    bigSmallData.setModulus_id(result.getBig_small_modulus_id());
                    //更新
                    bigSmallDao.updateBigSmallData(bigSmallData);
                }

            }
//        }

    }

    @Override
    @Transactional
    public List<BigSmallDataResult> getAllBigSmallResultData() {
        List<BigSmallDataResult> bigSmallDataResults = new ArrayList<>();
        List<BigSmallData> bigSmallDataList = bigSmallDao.getBigSmallData();
        for(BigSmallData bigSmallData : bigSmallDataList){
            BigSmallDataResult bigSmallDataResult = new BigSmallDataResult();
            BeanUtils.copyProperties(bigSmallData,bigSmallDataResult);
            bigSmallDataResult.setMatch_time_str(DateUtils.DateFormatString(bigSmallDataResult.getMatch_time()));
            if(StringUtils.isNotBlank(bigSmallDataResult.getModulus_id())){
                BigSmallModulus bigSmallModulus = bigSmallModulusDao.getBigSmallModulusById(bigSmallDataResult.getModulus_id());
                bigSmallDataResult.setBigSmallModulus(bigSmallModulus);
            }
            bigSmallDataResults.add(bigSmallDataResult);
        }
        return bigSmallDataResults;
    }

    @Override
    public List<BigSmallDataResult> getBigSmallResultDataBy(BigSmallData query) {
        List<BigSmallDataResult> bigSmallDataResults = new ArrayList<>();
        List<BigSmallData> bigSmallDataList = bigSmallDao.getBigSmallDataBy(query);
        for(BigSmallData bigSmallData : bigSmallDataList){
            BigSmallDataResult bigSmallDataResult = new BigSmallDataResult();
            BeanUtils.copyProperties(bigSmallData,bigSmallDataResult);
            bigSmallDataResult.setMatch_time_str(DateUtils.DateFormatString(bigSmallDataResult.getMatch_time()));
            if(StringUtils.isNotBlank(bigSmallDataResult.getModulus_id())){
                BigSmallModulus bigSmallModulus = bigSmallModulusDao.getBigSmallModulusById(bigSmallDataResult.getModulus_id());
                bigSmallDataResult.setBigSmallModulus(bigSmallModulus);
            }
            bigSmallDataResults.add(bigSmallDataResult);
        }
        return bigSmallDataResults;
    }

    @Override
    public List<BigSmallDataCompareResult> getBigSmallCompareDataBy(BigSmallData query) {
        List<BigSmallDataCompareResult> bigSmallDataCompareResults = new ArrayList<>();
        query.setCompany_name("韦德");
        List<BigSmallData> bigSmallDataList = bigSmallDao.getBigSmallDataBy(query);
        for(BigSmallData bigSmallData : bigSmallDataList){
            BigSmallDataCompareResult bigSmallDataCompareResult = new BigSmallDataCompareResult();
            BeanUtils.copyProperties(bigSmallData,bigSmallDataCompareResult);
            bigSmallDataCompareResult.setMatch_time_str(DateUtils.DateFormatString(bigSmallDataCompareResult.getMatch_time()));

            BigSmallDataResult bigSmallDataResult_WD = new BigSmallDataResult();
            BeanUtils.copyProperties(bigSmallData,bigSmallDataResult_WD);
            if(StringUtils.isNotBlank(bigSmallDataResult_WD.getModulus_id())){
                BigSmallModulus bigSmallModulus = bigSmallModulusDao.getBigSmallModulusById(bigSmallDataResult_WD.getModulus_id());
                bigSmallDataResult_WD.setBigSmallModulus(bigSmallModulus);
            }
            bigSmallDataCompareResult.setBigSmallDataResult_WD(bigSmallDataResult_WD);

            BigSmallData queryBigSmallData = new BigSmallData();
            queryBigSmallData.setCompany_name("Bet365");
            queryBigSmallData.setMatch_id(bigSmallData.getMatch_id());
            BigSmallData bigSmallData_365 =bigSmallDao.getBigSmallDataByMatchId(queryBigSmallData);

            BigSmallDataResult bigSmallDataResult_365 = new BigSmallDataResult();
            if(bigSmallData_365 != null){
                BeanUtils.copyProperties(bigSmallData_365,bigSmallDataResult_365);
            }
            if(StringUtils.isNotBlank(bigSmallDataResult_365.getModulus_id())){
                BigSmallModulus bigSmallModulus = bigSmallModulusDao.getBigSmallModulusById(bigSmallDataResult_365.getModulus_id());
                bigSmallDataResult_365.setBigSmallModulus(bigSmallModulus);
            }
            bigSmallDataCompareResult.setBigSmallDataResult_365(bigSmallDataResult_365);
            bigSmallDataCompareResults.add(bigSmallDataCompareResult);
        }
        return bigSmallDataCompareResults;
    }




    @Override
    public void analyseBigSmallBet() {
        //获取大小球数据
        List<BigSmallData> bigSmallDataList = bigSmallDao.getBigSmallHistoryTestData();
        for(BigSmallData bigSmallData :  bigSmallDataList){
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
            BigDecimal hostGetGoal = hostGoalData.getGetGoal().divide(hostGoalData.getGameNumber(),2,BigDecimal.ROUND_HALF_UP);
//            hostGetGoal = hostGetGoal.multiply(hostGet);
            BigDecimal hostLostGoal = hostGoalData.getLostGoal().divide(hostGoalData.getGameNumber(),2,BigDecimal.ROUND_HALF_UP);
//            hostLostGoal = hostLostGoal.multiply(hostLost);

            BigDecimal guestGetGoal = guestGoalData.getGetGoal().divide(guestGoalData.getGameNumber(),2,BigDecimal.ROUND_HALF_UP);
//            guestGetGoal = guestGetGoal.multiply(guestGet);
            BigDecimal guestLostGoal = hostGoalData.getLostGoal().divide(guestGoalData.getGameNumber(),2,BigDecimal.ROUND_HALF_UP);
//            guestLostGoal = guestLostGoal.multiply(guestLost);

            perCount = hostGetGoal.add(hostLostGoal).add(guestGetGoal).add(guestLostGoal);
            bigSmallData.setPer_bet(perCount);

            bigSmallDao.updateBigSmallTestData(bigSmallData);
        }


    }


}
