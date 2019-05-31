package com.shenchen.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class BigSmallModulus implements Serializable {
    private static final long serialVersionUID = 1188436762022698740L;

    //id
    private String big_small_modulus_id;

    //公司名称
    private String company_name;

    //联赛
    private String league_name_simply;

    //主队进球指数
    private BigDecimal host_get;

    //主队失球指数
    private BigDecimal host_lost;

    //客队进球指数
    private BigDecimal guest_get;

    //客队失球指数
    private BigDecimal guest_lost;

    //正买=1，反买=2
    private Integer reverse;

    //历史总场次
    private Integer total_count;

    //指数胜场次
    private Integer win_count;

    //指数亏场次
    private Integer lost_count;

    //指数购买
    private Integer buy_count;

    //购买一元盈利数
    private BigDecimal sum;

    //收益率
    private BigDecimal rate;

    //胜率
    private BigDecimal win_rate;

    //创建日期
    private Date gmt_create;

}