package com.shenchen.service.impl;

import com.shenchen.dao.IBigSmallDao;
import com.shenchen.dao.IBigSmallModulusDao;
import com.shenchen.model.BigSmallData;
import com.shenchen.model.BigSmallModulus;
import com.shenchen.model.BigSmallModulusMaxAndMin;
import com.shenchen.service.IBigSmallModulusService;
import com.shenchen.service.IBigSmallService;
import com.shenchen.util.GameCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("bigSmallModulusService")
public class BigSmallModulusServiceImpl implements IBigSmallModulusService {

    protected final static Logger logger = Logger.getLogger(BigSmallModulusServiceImpl.class);

    @Autowired
    IBigSmallModulusDao bigSmallModulusDao;

    @Autowired
    IBigSmallService bigSmallService;

    @Autowired
    IBigSmallDao bigSmallDao;

    @Override
    public Integer insertBigSmallModulus(BigSmallModulus bigSmallModulus) {
        return bigSmallModulusDao.insertBigSmallModulus(bigSmallModulus);
    }

    @Override
    public Integer deleteAllBigSmallModulus() {
        return bigSmallModulusDao.deleteAllBigSmallModulus();
    }

    @Override
    @Scheduled(cron="0 0 6 ? * WED")//
    public void createBigSmallModulus() {
        logger.info("大小球计算开始时间" + new Date());
        long startTime =  System.currentTimeMillis();
        List<String> gameNames = GameCheckUtils.gameList();
        for(String gameName : gameNames){
            List<BigSmallModulus> result = new ArrayList<>();
            BigSmallModulus maxData = null;
            List<BigSmallModulus> fanResult = bigSmallService.analyseBigSmallEfficient(gameName,true);
            List<BigSmallModulus> zhengResult = bigSmallService.analyseBigSmallEfficient(gameName,false);
            result.addAll(fanResult);
            result.addAll(zhengResult);
            for(BigSmallModulus bigSmallModulus : result ){
                if(maxData == null){
                    maxData = bigSmallModulus;
                }
                if(bigSmallModulus.getWin_rate().doubleValue() > maxData.getWin_rate().doubleValue()){
                    maxData = bigSmallModulus;
                }
            }
            if(maxData != null){
                bigSmallModulusDao.insertBigSmallModulus(maxData);
            }
        }
        logger.info("zhs" + (System.currentTimeMillis() - startTime));
    }




}
