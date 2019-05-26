package com.shenchen.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GameBean {
    //比赛唯一ID
    private String ID;

    //比赛状态
    private String MATCH_STATUS;

    //联赛
    private String LEAGUE_NAME_SIMPLY;

    //主队名称
    private String HOST_NAME;

    //客队名称
    private String GUEST_NAME;

    //比赛时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String MATCH_TIME;

    //主队得分
    private Integer HOST_GOAL;

    //客队得分
    private Integer GUEST_GOAL;

    private List<CompanyOdd> listOdds;


    @Override
    public String toString() {
        return "GameBean{" +
                "ID='" + ID + '\'' +
                ", LEAGUE_NAME_SIMPLY='" + LEAGUE_NAME_SIMPLY + '\'' +
                ", HOST_NAME='" + HOST_NAME + '\'' +
                ", GUEST_NAME='" + GUEST_NAME + '\'' +
                ", MATCH_TIME='" + MATCH_TIME + '\'' +
                ", HOST_GOAL=" + HOST_GOAL +
                ", GUEST_GOAL=" + GUEST_GOAL +
                ", listOdds=" + listOdds +
                '}';
    }
}