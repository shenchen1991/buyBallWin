package com.shenchen.service.impl;

import com.shenchen.dao.IBigSmallModulusDao;
import com.shenchen.model.BigSmallModulus;
import com.shenchen.service.IBigSmallModulusService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("bigSmallModulusService")
public class BigSmallModulusServiceImpl implements IBigSmallModulusService {

    protected final static Logger logger = Logger.getLogger(BigSmallModulusServiceImpl.class);

    @Autowired
    IBigSmallModulusDao bigSmallModulusDao;



    @Override
    public Integer insertBigSmallModulus(BigSmallModulus bigSmallModulus) {
        return bigSmallModulusDao.insertBigSmallModulus(bigSmallModulus);
    }

    @Override
    public Integer deleteAllBigSmallModulus() {
        return bigSmallModulusDao.deleteAllBigSmallModulus();
    }
}
