package com.shenchen.service.impl;

import com.shenchen.dao.IAsiansDao;
import com.shenchen.model.*;
import com.shenchen.service.IAsiansService;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("asiansService")
public class AsiansServiceImpl implements IAsiansService {

    protected final static Logger logger = Logger.getLogger(AsiansServiceImpl.class);

    @Autowired
    IAsiansDao asiansDao;

    @Override
    public List<AsiansHistoryData> getHostAsiansData(AnalyseQuery analyseQuery) {
        return asiansDao.getHostAsiansData(analyseQuery);
    }

    @Override
    public AsiansHostRateResult getHostAsiansRateResult(AnalyseQuery analyseQuery) {
        AsiansHostRateResult asiansHostRateResult = new AsiansHostRateResult();
        BeanUtils.copyProperties(analyseQuery,asiansHostRateResult);
        List<AsiansHistoryData> asiansHistoryDataList = asiansDao.getHostAsiansData(analyseQuery);
        asiansHostRateResult.setAsiansHistoryDataList(asiansHistoryDataList);
        asiansHostRateResult.setTotal_game(asiansHistoryDataList.size());
        for(AsiansHistoryData asiansHistoryData : asiansHistoryDataList){
            if(asiansHistoryData.getBuy_host().doubleValue() == 1D){
                asiansHostRateResult.setWater_game(asiansHostRateResult.getWater_game() + 1);
            }else if(asiansHistoryData.getBuy_host().doubleValue() > 1D){
                asiansHostRateResult.setHost_win_game(asiansHostRateResult.getHost_win_game() + 1);
            }else{
                asiansHostRateResult.setGuest_win_game(asiansHostRateResult.getGuest_win_game() + 1);
            }
        }
        return asiansHostRateResult;
    }

    @Override
    public List<AsiansHistoryData> getGuestAsiansData(AnalyseQuery analyseQuery) {
        return asiansDao.getGuestAsiansData(analyseQuery);
    }


    @Override
    public AsiansGuestRateResult getGuestAsiansRateResult(AnalyseQuery analyseQuery) {
        AsiansGuestRateResult asiansGuestRateResult = new AsiansGuestRateResult();
        BeanUtils.copyProperties(analyseQuery,asiansGuestRateResult);
        List<AsiansHistoryData> asiansHistoryDataList = asiansDao.getGuestAsiansData(analyseQuery);
        asiansGuestRateResult.setAsiansHistoryDataList(asiansHistoryDataList);
        asiansGuestRateResult.setTotal_game(asiansHistoryDataList.size());
        for(AsiansHistoryData asiansHistoryData : asiansHistoryDataList){
            if(asiansHistoryData.getBuy_guest().doubleValue() == 1D){
                asiansGuestRateResult.setWater_game(asiansGuestRateResult.getWater_game() + 1);
            }else if(asiansHistoryData.getBuy_guest().doubleValue() > 1D){
                asiansGuestRateResult.setGuest_win_game(asiansGuestRateResult.getGuest_win_game() + 1);
            }else{
                asiansGuestRateResult.setHost_win_game(asiansGuestRateResult.getHost_win_game() + 1);
            }
        }
        return asiansGuestRateResult;
    }
}
