//package com.shenchen.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("/bigSmall")
//public class BigSmallController {
//
//    @Autowired
//    private IBigSmallService bigSmallService;
//
//    @RequestMapping(value="/getBigSmallData.do",method=RequestMethod.GET)
//    public String getBigSmallData(){
//        bigSmallService.getBigSmallData();
//        return "getBigSmallData";
//    }
//
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
//
//
//}