package com.shenchen.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class BigSmallDataCompareResult implements Serializable {
    private static final long serialVersionUID = 1188436762022698740L;

    //比赛id
    private String match_id;

    //联赛
    private String league_name_simply;

    //主队名称
    private String host_name;

    //客队名称
    private String guest_name;

    //比赛时间
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date match_time;

    //比赛时间
    private String match_time_str;

    //主队得分
    private Integer host_goal;

    //客队得分
    private Integer guest_goal;

    //总得分
    private Integer total_goal;

    //比赛结果
    private Integer game_result;

    private BigSmallDataResult bigSmallDataResult_WD;

    private BigSmallDataResult bigSmallDataResult_365;

}