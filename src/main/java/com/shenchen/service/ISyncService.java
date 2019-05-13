package com.shenchen.service;

import java.io.IOException;

public interface ISyncService {

    void syncDataFromNet();

    void syncBaseDataFromNet();

    void syncBigSmallDataFromNet();

    void syncRankData() throws IOException;
}