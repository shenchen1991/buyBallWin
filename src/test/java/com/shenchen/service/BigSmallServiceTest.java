package com.shenchen.service;

import com.shenchen.TestSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BigSmallServiceTest extends TestSupport {

    @Autowired
    IBigSmallService bigSmallService;

    @Autowired
    IBigSmallModulusService bigSmallModulusService;

    @Test
    public void calculationResultTest(){
       bigSmallService.calculationResult();
    }

    @Test
    public void analyseBigSmallBetTest(){
        bigSmallService.analyseBigSmallBet();
    }


}
