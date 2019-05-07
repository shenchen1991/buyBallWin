package com.shenchen.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class EuropeHistoryData implements Serializable {
    private static final long serialVersionUID = 1188436762022698740L;

    private String europe_history_data_id;

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

    //比赛结果
    private Integer game_result;

    //公司名称
    private String company_name;

    //初始主胜赔率
    private BigDecimal first_win;

    //初始平局赔率
    private BigDecimal first_same;

    //初始客胜赔率
    private BigDecimal first_lost;

    //即时主胜赔率
    private BigDecimal win;

    //即时平局赔率
    private BigDecimal same;

    //即时客胜赔率
    private BigDecimal lost;

    //购买主胜
    private BigDecimal buy_win = new BigDecimal(0);

    //购买平局
    private BigDecimal buy_same = new BigDecimal(0);

    //购买客场
    private BigDecimal buy_lost = new BigDecimal(0);


    @Override
    public String toString() {
        return "EuropeHistoryData{" +
                "europe_history_data_id='" + europe_history_data_id + '\'' +
                ", match_id='" + match_id + '\'' +
                ", league_name_simply='" + league_name_simply + '\'' +
                ", host_name='" + host_name + '\'' +
                ", guest_name='" + guest_name + '\'' +
                ", match_time=" + match_time +
                ", match_time_str='" + match_time_str + '\'' +
                ", host_goal=" + host_goal +
                ", guest_goal=" + guest_goal +
                ", game_result=" + game_result +
                ", company_name='" + company_name + '\'' +
                ", first_win=" + first_win +
                ", first_same=" + first_same +
                ", first_lost=" + first_lost +
                ", win=" + win +
                ", same=" + same +
                ", lost=" + lost +
                ", buy_win=" + buy_win +
                ", buy_same=" + buy_same +
                ", buy_lost=" + buy_lost +
                '}';
    }
}