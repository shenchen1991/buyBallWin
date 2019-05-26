package com.shenchen.controller;

import com.shenchen.model.BigSmallDataResult;
import com.shenchen.service.IBigSmallService;
import com.shenchen.service.ISyncService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/sync")
public class SyncController {

    protected final static Logger logger = Logger.getLogger(SyncController.class);

    @Autowired
    private ISyncService syncService;

    @RequestMapping(value="/syncDataFromNetIncrement.do",method=RequestMethod.GET)
    public String syncDataFromNetIncrement(){
        try{
            syncService.syncDataFromNetIncrement();
        }catch (Exception e){
            logger.error("规律计算异常",e);
            return "fail";
        }
        return "success";

    }
}