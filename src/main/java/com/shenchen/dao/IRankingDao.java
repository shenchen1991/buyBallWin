package com.shenchen.dao;

import com.shenchen.model.AnalyseQuery;
import com.shenchen.model.TeamRankingData;

public interface IRankingDao {

    Integer insertTeamRankingData(TeamRankingData teamRankingData);


    TeamRankingData getHostTeamRanking(AnalyseQuery analyseQuery);

    TeamRankingData getGuestTeamRanking(AnalyseQuery analyseQuery);
}