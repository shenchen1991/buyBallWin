package com.shenchen.dao;

import com.shenchen.model.EuropeHistoryData;

public interface IEuropeDao {

    Integer insertEuropeHistoryData(EuropeHistoryData europeHistoryData);

    Integer deleteEuropeHistoryData();
}