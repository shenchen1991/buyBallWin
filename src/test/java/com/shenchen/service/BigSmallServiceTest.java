package com.shenchen.service;

import com.shenchen.TestSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BigSmallServiceTest extends TestSupport {

    @Autowired
    IBigSmallService bigSmallService;

    @Test
    public void getAllBigSmallResultDataTest(){
        bigSmallService.getAllBigSmallResultData();
    }

    @Test
    public void calculationResultTest(){
       bigSmallService.calculationResult();
    }

    @Test
    public void analyseBigSmallBetTest(){
        bigSmallService.analyseBigSmallBet();
    }


}
