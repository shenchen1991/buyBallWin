package com.shenchen.dao;

import com.shenchen.model.BigSmallData;

import java.util.List;

public interface IBigSmallDao {

    Integer insertBigSmallHistoryData(BigSmallData bigSmallData);

    List<BigSmallData> getBigSmallHistoryData();

    Integer deleteBigSmallHistoryDataBy(BigSmallData bigSmallData);

    Integer deleteAllBigSmallHistoryData();

    Integer updateBigSmallHistoryData(BigSmallData bigSmallData);

    Integer updateAllBigSmallHistoryData();

    Integer updateBigSmallHistoryDataByLeague(String league_name_simply);


    Integer insertBigSmallData(BigSmallData bigSmallData);

    Integer deleteBigSmallNewDataBy(BigSmallData bigSmallData);

    List<BigSmallData> getBigSmallData();

    List<BigSmallData> getBigSmallDataBy(BigSmallData bigSmallData);

    BigSmallData getBigSmallDataByMatchId(String match_id);

    Integer updateBigSmallData(BigSmallData bigSmallData);

}