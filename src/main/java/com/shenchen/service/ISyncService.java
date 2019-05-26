package com.shenchen.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public interface ISyncService {

    void syncDataFromNet();

    void syncBaseDataFromNet();

    void syncBigSmallDataFromNet();

    @Transactional
    @Scheduled(cron="0 0 4 * * ?")
    void syncDataFromNetIncrement();

    void syncRankData() throws IOException;
}