package com.shenchen.dao;

import com.shenchen.model.BigSmallData;

import java.util.List;

public interface IBigSmallDao {

    Integer insertBigSmallData(BigSmallData bigSmallData);

    Integer deleteAllBigSmallData();

    Integer updateBigSmallData(BigSmallData bigSmallData);

    Integer updateAllBigSmallData();

    Integer updateBigSmallDataByLeague(String league_name_simply);

    List<BigSmallData> getBigSmallData();
}