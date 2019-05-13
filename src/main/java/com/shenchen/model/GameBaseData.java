package com.shenchen.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class GameBaseData implements Serializable {
    private static final long serialVersionUID = 1188436762022698740L;

    private String game_base_data_id;

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

    @Override
    public String toString() {
        return "GameBaseData{" +
                "game_base_data_id='" + game_base_data_id + '\'' +
                ", match_id='" + match_id + '\'' +
                ", league_name_simply='" + league_name_simply + '\'' +
                ", host_name='" + host_name + '\'' +
                ", guest_name='" + guest_name + '\'' +
                ", match_time=" + match_time +
                ", match_time_str='" + match_time_str + '\'' +
                ", host_goal=" + host_goal +
                ", guest_goal=" + guest_goal +
                ", game_result=" + game_result +
                '}';
    }
}