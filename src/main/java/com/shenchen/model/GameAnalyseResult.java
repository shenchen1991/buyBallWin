package com.shenchen.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GameAnalyseResult implements Serializable {
    private static final long serialVersionUID = 1188436762022698740L;

    //比赛信息
    private GameBean gameBean;

    //主队伍名次信息
    private TeamRankingData hostTeamRanking;

    //客队伍名次信息
    private TeamRankingData guestTeamRanking;

    //主队亚赔历史数据分析
    private AsiansHostRateResult asiansHostRateResult;

    //客队亚赔历史数据分析
    private AsiansGuestRateResult asiansGuestRateResult;
}