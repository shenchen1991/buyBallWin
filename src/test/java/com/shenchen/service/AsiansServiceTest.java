package com.shenchen.service;

import com.shenchen.TestSupport;
import com.shenchen.model.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AsiansServiceTest extends TestSupport {

    @Autowired
    IAsiansService asiansService;

    @Test
    public void testGetHostAsiansData(){
        AnalyseQuery analyseQuery = new AnalyseQuery();
        analyseQuery.setLeague_name_simply("西甲");
        analyseQuery.setHost_name("韦斯卡");
        List<AsiansHistoryData> asiansHistoryDataList = asiansService.getHostAsiansData(analyseQuery);
        System.out.println(asiansHistoryDataList.size());
    }

    @Test
    public void testGetHostAsiansRateResult(){
        AnalyseQuery analyseQuery = new AnalyseQuery();
        analyseQuery.setLeague_name_simply("西甲");
        analyseQuery.setHost_name("韦斯卡");
        AsiansHostRateResult asiansHostRateResult = asiansService.getHostAsiansRateResult(analyseQuery);
        System.out.println(asiansHostRateResult);
    }

    @Test
    public void testGetGuestAsiansData(){
        AnalyseQuery analyseQuery = new AnalyseQuery();
        analyseQuery.setLeague_name_simply("西甲");
        analyseQuery.setGuest_name("巴伦西亚");
        List<AsiansHistoryData> asiansHistoryDataList = asiansService.getGuestAsiansData(analyseQuery);
        System.out.println(asiansHistoryDataList.size());
    }

    @Test
    public void testGetGuestAsiansRateResult(){
        AnalyseQuery analyseQuery = new AnalyseQuery();
        analyseQuery.setLeague_name_simply("西甲");
        analyseQuery.setGuest_name("巴伦西亚");
        AsiansGuestRateResult asiansGuestRateResult = asiansService.getGuestAsiansRateResult(analyseQuery);
        System.out.println(asiansGuestRateResult);
    }
}
