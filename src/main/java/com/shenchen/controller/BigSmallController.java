package com.shenchen.controller;

import com.shenchen.model.*;
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
    public List<BigSmallDataResult> getBigSmallDataBy(Integer isEnd,Integer isBuy,String company){
        BigSmallData query = new BigSmallData();
        query.setIs_end(isEnd);
        query.setIsBuy(isBuy);
        query.setCompany_name(company);
        return bigSmallService.getBigSmallResultDataBy(query);
    }


    @RequestMapping(value="/countBigSmallDataBy.do",method=RequestMethod.GET)
    public BigSmallCountResult countBigSmallDataBy(Integer isEnd, Integer isBuy,String company){
        BigSmallCountResult bigSmallCountResult = new BigSmallCountResult();
        BigSmallData query = new BigSmallData();
        query.setIs_end(isEnd);
        query.setIsBuy(isBuy);
        query.setCompany_name(company);
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
            logger.error("预测异常",e);
            return "fail";
        }
        return "success";

    }



    @RequestMapping(value="/getBigSmallCompareDataBy.do",method=RequestMethod.GET)
    public List<BigSmallDataCompareResult> getBigSmallCompareDataBy(Integer isEnd){
        BigSmallData query = new BigSmallData();
        query.setIs_end(isEnd);
        return bigSmallService.getBigSmallCompareDataBy(query);
    }


    @RequestMapping(value="/countBigSmallCompareDataBy.do",method=RequestMethod.GET)
    public BigSmallCountResult countBigSmallCompareDataBy(Integer isEnd){
        BigSmallCountResult bigSmallCountResult = new BigSmallCountResult();
        BigSmallData query = new BigSmallData();
        query.setIs_end(isEnd);
        List<BigSmallDataCompareResult> result= bigSmallService.getBigSmallCompareDataBy(query);
        for(BigSmallDataCompareResult bigSmallDataCompareResult : result){
            BigSmallDataResult bigSmallDataResult_WD = bigSmallDataCompareResult.getBigSmallDataResult_WD();
            BigSmallDataResult bigSmallDataResult_365 = bigSmallDataCompareResult.getBigSmallDataResult_365();

            if(bigSmallDataResult_WD.getModulus_id() != null
                    && bigSmallDataResult_365.getModulus_id() == null){
                if(bigSmallDataResult_WD.getBuy_result_real() != null){
                    if(bigSmallDataResult_WD.getBuy_result_real().doubleValue() > 0 ){
                        bigSmallCountResult.setWinCount(bigSmallCountResult.getWinCount() + 1);
                    }else if(bigSmallDataResult_WD.getBuy_result_real().doubleValue() == 0 ){
                        bigSmallCountResult.setWaterCount(bigSmallCountResult.getWaterCount() + 1);
                    }else if(bigSmallDataResult_WD.getBuy_result_real().doubleValue() < 0 ){
                        bigSmallCountResult.setLostCount(bigSmallCountResult.getLostCount() + 1);
                    }
                }
            }else if(bigSmallDataResult_WD.getModulus_id() != null
                    && bigSmallDataResult_365.getModulus_id() != null){
                if(bigSmallDataResult_WD.getBigSmallModulus().getWin_rate().doubleValue()
                        > bigSmallDataResult_365.getBigSmallModulus().getWin_rate().doubleValue()){
                    if(bigSmallDataResult_WD.getBuy_result_real() != null){
                        if(bigSmallDataResult_WD.getBuy_result_real().doubleValue() > 0 ){
                            bigSmallCountResult.setWinCount(bigSmallCountResult.getWinCount() + 1);
                        }else if(bigSmallDataResult_WD.getBuy_result_real().doubleValue() == 0 ){
                            bigSmallCountResult.setWaterCount(bigSmallCountResult.getWaterCount() + 1);
                        }else if(bigSmallDataResult_WD.getBuy_result_real().doubleValue() < 0 ){
                            bigSmallCountResult.setLostCount(bigSmallCountResult.getLostCount() + 1);

                        }
                    }
                }else {
                    if(bigSmallDataResult_365.getBuy_result_real() != null){
                        if(bigSmallDataResult_365.getBuy_result_real().doubleValue() > 0 ){
                            bigSmallCountResult.setWinCount(bigSmallCountResult.getWinCount() + 1);
                        }else if(bigSmallDataResult_365.getBuy_result_real().doubleValue() == 0 ){
                            bigSmallCountResult.setWaterCount(bigSmallCountResult.getWaterCount() + 1);
                        }else if(bigSmallDataResult_365.getBuy_result_real().doubleValue() < 0 ){
                            bigSmallCountResult.setLostCount(bigSmallCountResult.getLostCount() + 1);

                        }
                    }
                }
            }else if (bigSmallDataResult_WD.getModulus_id() == null
                    && bigSmallDataResult_365.getModulus_id() != null){
                if(bigSmallDataResult_365.getBuy_result_real() != null){
                    if(bigSmallDataResult_365.getBuy_result_real().doubleValue() > 0 ){
                        bigSmallCountResult.setWinCount(bigSmallCountResult.getWinCount() + 1);
                    }else if(bigSmallDataResult_365.getBuy_result_real().doubleValue() == 0 ){
                        bigSmallCountResult.setWaterCount(bigSmallCountResult.getWaterCount() + 1);
                    }else if(bigSmallDataResult_365.getBuy_result_real().doubleValue() < 0 ){
                        bigSmallCountResult.setLostCount(bigSmallCountResult.getLostCount() + 1);
                    }
                }
            }
        }
        return bigSmallCountResult;
    }
}