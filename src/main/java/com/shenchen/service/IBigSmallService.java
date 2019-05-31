package com.shenchen.service;

import com.shenchen.model.BigSmallData;
import com.shenchen.model.BigSmallDataCompareResult;
import com.shenchen.model.BigSmallDataResult;
import com.shenchen.model.BigSmallModulus;

import java.util.List;

public interface IBigSmallService {

    List<BigSmallModulus> analyseBigSmallEfficient(String league_name_simply,String company, boolean reverse);

    void calculationResult();

    List<BigSmallDataResult> getAllBigSmallResultData();

    List<BigSmallDataResult> getBigSmallResultDataBy(BigSmallData bigSmallData);

    List<BigSmallDataCompareResult> getBigSmallCompareDataBy(BigSmallData bigSmallData);

    void analyseBigSmallBet();
}