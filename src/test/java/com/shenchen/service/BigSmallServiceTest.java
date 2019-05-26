package com.shenchen.service;

import com.shenchen.TestSupport;
import com.shenchen.model.BigSmallModulus;
import com.shenchen.model.BigSmallModulusMaxAndMin;
import com.shenchen.util.GameCheckUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class BigSmallServiceTest extends TestSupport {

    @Autowired
    IBigSmallService bigSmallService;

    @Autowired
    IBigSmallModulusService bigSmallModulusService;

    @Test
    public void calculationResultTest(){
       bigSmallService.calculationResult();
    }


}
