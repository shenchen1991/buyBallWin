package com.shenchen.dao;

import com.shenchen.model.BigSmallModulus;

public interface IBigSmallModulusDao {

    Integer insertBigSmallModulus(BigSmallModulus bigSmallModulus);

    Integer deleteAllBigSmallModulus();

    BigSmallModulus getOneBigSmallModulus(BigSmallModulus bigSmallModulus);

    BigSmallModulus getBigSmallModulusById(String big_small_modulus_id);

}