package com.shenchen.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CombinationData implements Serializable {
    private static final long serialVersionUID = 1188436762022698740L;

    private Integer numberOne;

    private Integer numberTwo;

    private Integer numberThree;

    private Integer numberFour;

    private Integer numberFive;

    private Integer numberTotal;

    private Integer numberEnd;

    private Integer numberTotalDouble;

    private Integer numberEndDouble;

}