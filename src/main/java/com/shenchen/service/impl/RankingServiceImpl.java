package com.shenchen.service.impl;

import com.shenchen.dao.IRankingDao;
import com.shenchen.model.AnalyseQuery;
import com.shenchen.model.TeamRankingData;
import com.shenchen.service.IRankingService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("rankingService")
public class RankingServiceImpl implements IRankingService {

    protected final static Logger logger = Logger.getLogger(RankingServiceImpl.class);

    @Autowired
    IRankingDao rankingDao;


    @Override
    public TeamRankingData getHostTeamRanking(AnalyseQuery analyseQuery) {
        return rankingDao.getHostTeamRanking(analyseQuery);
    }

    @Override
    public TeamRankingData getGuestTeamRanking(AnalyseQuery analyseQuery) {
        return rankingDao.getGuestTeamRanking(analyseQuery);
    }
}
