package com.shenchen.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BigSmallResultDatas implements Serializable {
    private static final long serialVersionUID = 1188436762022698740L;

    //联赛
    private String league_name_simply;

    BigSmallResultData maxData;
    BigSmallResultData minData ;
}