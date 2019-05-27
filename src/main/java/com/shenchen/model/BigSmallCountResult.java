package com.shenchen.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BigSmallCountResult implements Serializable {

    private static final long serialVersionUID = 1188436762022698740L;

    private Integer winCount = 0;

    private Integer waterCount = 0;

    private Integer lostCount = 0;
}