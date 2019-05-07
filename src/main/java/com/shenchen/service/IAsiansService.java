package com.shenchen.service;

import com.shenchen.model.*;

import java.util.List;

public interface IAsiansService {


    List<AsiansHistoryData> getHostAsiansData(AnalyseQuery asiansHistoryQuery);

    AsiansHostRateResult getHostAsiansRateResult(AnalyseQuery asiansHistoryQuery);

    List<AsiansHistoryData> getGuestAsiansData(AnalyseQuery asiansHistoryQuery);

    AsiansGuestRateResult getGuestAsiansRateResult(AnalyseQuery asiansHistoryQuery);
}