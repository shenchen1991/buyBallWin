package com.shenchen.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AnalyseQuery implements Serializable {
    private static final long serialVersionUID = 1188436762022698740L;


    //联赛
    private String league_name_simply;

    //主队名称
    private String host_name;

    //客队名称
    private String guest_name;

    @Override
    public String toString() {
        return "AnalyseQuery{" +
                "league_name_simply='" + league_name_simply + '\'' +
                ", host_name='" + host_name + '\'' +
                ", guest_name='" + guest_name + '\'' +
                '}';
    }
}