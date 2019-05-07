package com.shenchen.service;

import com.shenchen.model.AnalyseQuery;
import com.shenchen.model.TeamRankingData;

public interface IRankingService {

    TeamRankingData getHostTeamRanking(AnalyseQuery analyseQuery);

    TeamRankingData getGuestTeamRanking(AnalyseQuery analyseQuery);
}