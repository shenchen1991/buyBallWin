package com.shenchen.controller;

import com.shenchen.model.BigSmallCountResult;
import com.shenchen.model.BigSmallData;
import com.shenchen.model.BigSmallDataResult;
import com.shenchen.service.IBigSmallService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/bigSmall")
public class BigSmallController {

    protected final static Logger logger = Logger.getLogger(BigSmallController.class);

    @Autowired
    private IBigSmallService bigSmallService;

    @RequestMapping(value="/getAllBigSmallData.do",method=RequestMethod.GET)
    public List<BigSmallDataResult> getBigSmallData(){
        return bigSmallService.getAllBigSmallResultData();
    }

    @RequestMapping(value="/getBigSmallDataBy.do",method=RequestMethod.GET)
    public List<BigSmallDataResult> getBigSmallDataBy(Integer isEnd,Integer isBuy){
        BigSmallData query = new BigSmallData();
        query.setIs_end(isEnd);
        query.setIsBuy(isBuy);
        return bigSmallService.getBigSmallResultDataBy(query);
    }


    @RequestMapping(value="/countBigSmallDataBy.do",method=RequestMethod.GET)
    public BigSmallCountResult countBigSmallDataBy(Integer isEnd, Integer isBuy){
        BigSmallCountResult bigSmallCountResult = new BigSmallCountResult();
        BigSmallData query = new BigSmallData();
        query.setIs_end(isEnd);
        query.setIsBuy(isBuy);
        List<BigSmallDataResult> result= bigSmallService.getBigSmallResultDataBy(query);
        for(BigSmallDataResult bigSmallDataResult : result){
            if(bigSmallDataResult.getBuy_result_real() != null){
                if(bigSmallDataResult.getBuy_result_real().doubleValue() > 0 ){
                    bigSmallCountResult.setWinCount(bigSmallCountResult.getWinCount() + 1);
                }else if(bigSmallDataResult.getBuy_result_real().doubleValue() == 0 ){
                    bigSmallCountResult.setWaterCount(bigSmallCountResult.getWaterCount() + 1);
                }else if(bigSmallDataResult.getBuy_result_real().doubleValue() < 0 ){
                    bigSmallCountResult.setLostCount(bigSmallCountResult.getLostCount() + 1);
                }
            }

        }
        return bigSmallCountResult;
    }

    @RequestMapping(value="/calculationResult.do",method=RequestMethod.GET)
    public String calculationResult(){
        try{
            bigSmallService.calculationResult();
        }catch (Exception e){
            logger.error("规律计算异常",e);
            return "fail";
        }
        return "success";

    }

//    @RequestMapping(value="/updateBigSmallData.do",method=RequestMethod.GET)
//    public String updateBigSmallData(){
//        bigSmallService.updateBigSmallData();
//        return "updateBigSmallData";
//    }
//
//    @RequestMapping(value="/updateGoalData.do",method=RequestMethod.GET)
//    public String updateGoalData(){
//        bigSmallService.updateGoalData();
//        return "updateGoalData";
//    }
//
//    @RequestMapping(value="/getBigSmallDataNew.do",method=RequestMethod.GET)
//    public String getBigSmallDataNew(){
//        bigSmallService.getBigSmallDataNew();
//        return "getBigSmallDataNew";
//    }


}