package com.shenchen.service.impl;

import com.shenchen.dao.IBigSmallDao;
import com.shenchen.dao.IGameBaseDao;
import com.shenchen.model.BigSmallData;
import com.shenchen.model.GameBaseData;
import com.shenchen.model.GuestGoalData;
import com.shenchen.model.HostGoalData;
import com.shenchen.service.IBigSmallService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;


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
            hostGetGoal = hostGetGoal.multiply(new BigDecimal(0.5));
            BigDecimal hostLostGoal = hostGoalData.getLostGoal().divide(hostGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
            hostLostGoal = hostLostGoal.multiply(new BigDecimal(0.5));

            BigDecimal guestGetGoal = guestGoalData.getGetGoal().divide(guestGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
            guestGetGoal = guestGetGoal.multiply(new BigDecimal(0.5));
            BigDecimal guestLostGoal = hostGoalData.getLostGoal().divide(guestGoalData.getGameNumber(),5,BigDecimal.ROUND_HALF_UP);
            guestLostGoal = guestLostGoal.multiply(new BigDecimal(0.5));


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
}
