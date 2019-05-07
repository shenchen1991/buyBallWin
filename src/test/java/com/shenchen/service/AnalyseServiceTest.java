package com.shenchen.service;

import com.shenchen.TestSupport;
import com.shenchen.model.GameAnalyseResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AnalyseServiceTest extends TestSupport {

    @Autowired
    IAnalyseService analyseService;

    @Test
    public void testAnalyseGame(){
        List<GameAnalyseResult>  analyseResults = analyseService.analyseGame();
        System.out.println(analyseResults.size());
    }
}
