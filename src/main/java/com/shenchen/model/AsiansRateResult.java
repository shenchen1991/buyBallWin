package com.shenchen.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AsiansRateResult implements Serializable {
    private static final long serialVersionUID = 1188436762022698740L;


    //联赛
    private String league_name_simply;

    //主队名称
    private String host_name;

    //客队名称
    private String guest_name;

    //总比赛场次
    private Integer total_game = 0;

    //主场赢盘场次
    private Integer host_win_game = 0;

    //走盘场次
    private Integer water_game = 0;

    //主场赢盘场次
    private Integer guest_win_game = 0;

    //近N场详细
    List<AsiansHistoryData> asiansHistoryDataList = new ArrayList<>();

    @Override
    public String toString() {
        return "AsiansRateResult{" +
                "league_name_simply='" + league_name_simply + '\'' +
                ", host_name='" + host_name + '\'' +
                ", guest_name='" + guest_name + '\'' +
                ", total_game=" + total_game +
                ", host_win_game=" + host_win_game +
                ", water_game=" + water_game +
                ", guest_win_game=" + guest_win_game +
                ", asiansHistoryDataList=" + asiansHistoryDataList +
                '}';
    }
}