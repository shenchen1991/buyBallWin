package com.shenchen.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class BigSmallResultData implements Serializable {
    private static final long serialVersionUID = 1188436762022698740L;

    //联赛
    private String league_name_simply;

    private double hostGet;

    private double hostLost;

    private double guestGet;

    private double guestLost;

    private Integer totalCount;

    private double sum;

    private Integer winCount;

    private Integer lostCount;

    private double rate;

    private double winRate;

    @Override
    public String toString() {
        return "BigSmallResultData{" +
                "league_name_simply='" + league_name_simply + '\'' +
                ", hostGet=" + hostGet +
                ", hostLost=" + hostLost +
                ", guestGet=" + guestGet +
                ", guestLost=" + guestLost +
                ", totalCount=" + totalCount +
                ", sum=" + sum +
                ", winCount=" + winCount +
                ", lostCount=" + lostCount +
                ", rate=" + rate +
                ", winRate=" + winRate +
                '}';
    }
}