package com.shenchen.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class GuestGoalData implements Serializable {
    private static final long serialVersionUID = 1188436762022698740L;

    //联赛
    private String league_name_simply;

    //队伍名称
    private String team_name;

    //客队进球
    private BigDecimal getGoal = new BigDecimal(0);

    //客队丢球
    private BigDecimal lostGoal = new BigDecimal(0);

    private BigDecimal gameNumber = new BigDecimal(0);

}