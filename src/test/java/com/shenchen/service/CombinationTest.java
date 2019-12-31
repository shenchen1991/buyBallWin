package com.shenchen.service;

import com.shenchen.TestSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CombinationTest extends TestSupport {

    @Autowired
    ICombinationDataService combinationDataService;

    @Test
    public void insertCombinationDataTest(){
        combinationDataService.insertCombinationData();
    }

}
