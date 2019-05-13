package com.shenchen.service;

import com.shenchen.TestSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class SyncServiceTest extends TestSupport {

    @Autowired
    ISyncService syncService;

    @Test
    public void testSyncDataFromNet(){
        syncService.syncDataFromNet();
    }

    @Test
    public void testSyncBaseDataFromNet(){
        syncService.syncBaseDataFromNet();
    }


    @Test
    public void testSyncBigSmallFromNet(){
        syncService.syncBigSmallDataFromNet();
    }

    @Test
    public void testSyncRankData() throws IOException {
        syncService.syncRankData();
    }
}
