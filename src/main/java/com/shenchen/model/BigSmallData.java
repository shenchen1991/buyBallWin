package com.shenchen.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class BigSmallData implements Serializable {
    private static final long serialVersionUID = 1188436762022698740L;

    private String big_small_data_id;

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

    //公司名称
    private String company_name;

    //初始大小球
    private BigDecimal first_let_big_small;

    //初始大球赔率
    private BigDecimal first_big;

    //初始小球赔率
    private BigDecimal first_small;

    //大小球
    private BigDecimal let_big_small;

    //大球赔率
    private BigDecimal big;

    //小球赔率
    private BigDecimal small;


    //买大球结果
    private BigDecimal buy_big;

    //买小球结果
    private BigDecimal buy_small;

    //规律预测 大球-1 小球-2
    private Integer big_small_pre;

    //购买结果
    private BigDecimal buy_result;



}