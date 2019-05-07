package com.shenchen.dao;

import com.shenchen.model.AnalyseQuery;
import com.shenchen.model.AsiansHistoryData;

import java.util.List;

public interface IAsiansDao {

    Integer insertAsiansHistoryData(AsiansHistoryData asiansHistoryData);

    List<AsiansHistoryData> getHostAsiansData(AnalyseQuery analyseQuery);

    List<AsiansHistoryData> getGuestAsiansData(AnalyseQuery analyseQuery);

    Integer deleteAsiansHistoryData();
}