package com.shenchen.service;

import java.io.IOException;

public interface ISyncService {

    void syncDataFromNet();

    void syncRankData() throws IOException;
}