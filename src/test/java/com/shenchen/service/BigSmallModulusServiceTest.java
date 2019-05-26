package com.shenchen.service;

import com.shenchen.TestSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BigSmallModulusServiceTest extends TestSupport {

    @Autowired
    IBigSmallModulusService bigSmallModulusService;

    @Test
    public void testSyncDataFromNet(){
        bigSmallModulusService.createBigSmallModulus();
    }



}
