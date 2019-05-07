package com.shenchen.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class TeamRankingData implements Serializable {
    private static final long serialVersionUID = 1188436762022698740L;

    private Integer team_ranking_data_id;

    //联赛
    private String league_name_simply;

    //队名称
    private String team_name;

    //排名
    private Integer rank;

    //赛季总场次
    private Integer total_game;

    //赛季胜场
    private Integer win_game;

    //赛季平局
    private Integer same_game;

    //赛季败场
    private Integer lost_game;

    private String remark;

    private Date gmt_create;

    @Override
    public String toString() {
        return "TeamRankingData{" +
                "team_ranking_data_id=" + team_ranking_data_id +
                ", league_name_simply='" + league_name_simply + '\'' +
                ", team_name='" + team_name + '\'' +
                ", rank=" + rank +
                ", total_game=" + total_game +
                ", win_game=" + win_game +
                ", same_game=" + same_game +
                ", lost_game=" + lost_game +
                ", remark='" + remark + '\'' +
                ", gmt_create=" + gmt_create +
                '}';
    }
}