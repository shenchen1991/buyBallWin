package com.shenchen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.shenchen.dao.*;
import com.shenchen.model.*;
import com.shenchen.service.ISyncService;
import com.shenchen.util.DateUtils;
import com.shenchen.util.GameCheckUtils;
import com.shenchen.util.HttpClient4;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service("syncService")
public class SyncServiceImpl implements ISyncService {

    protected final static Logger logger = Logger.getLogger(SyncServiceImpl.class);

    @Autowired
    IAsiansDao asiansDao;

    @Autowired
    IEuropeDao europeDao;

    @Autowired
    IBigSmallDao bigSmallDao;

    @Autowired
    IGameBaseDao gameBaseDao;

    @Autowired
    IRankingDao rankingDao;

    private static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    @Override
    @Transactional
    public void syncDataFromNet() {
//        singleThreadExecutor.execute(() -> {
            Calendar startCalendar = DateUtils.getYesterdayOfNumber(new Date(),-1);
            Calendar endCalendar = DateUtils.getYesterdayOfNumber(new Date(),-200);
//            Calendar endCalendar = DateUtils.getYesterdayOfNumber(new Date(),-50);

        //先移除所有数据
//            asiansDao.deleteAsiansHistoryData();
//            europeDao.deleteEuropeHistoryData();
            bigSmallDao.deleteAllBigSmallHistoryData();
//            gameBaseDao.deleteAllGameBaseData();
            while(startCalendar.getTime().getTime() > endCalendar.getTime().getTime()){
                List<GameBean> gameBeans;
                String dateStr = DateUtils.dayFormatString(startCalendar.getTime());
                logger.info("网站同步数据当前日期为："+ dateStr);
                try{
                    //数据获取
                    String json =  HttpClient4.doGet("http://odds.zgzcw.com/odds/oyzs_ajax.action?type=qb&issue="+dateStr+"&date=&companys=14");
                    logger.info("网站获取信息为：" + json);
                    gameBeans = JSONObject.parseArray(json, GameBean.class);
                    if(!CollectionUtils.isEmpty(gameBeans)){
                        for(GameBean gameBean : gameBeans){
                            if(!GameCheckUtils.gameCheck(gameBean.getLEAGUE_NAME_SIMPLY())){
                               continue;
                            }
                            //亚赔数据
//                            createAsiansHistoryData(gameBean);
                            //大小球数据
//                            createBigSmallHistoryData(gameBean);
                            //欧赔数据
//                            createEuropeHistoryData(gameBean);
                            //同步赛事基本信息
//                            createBaseGameData(gameBean);

                        }
                    }
                    startCalendar.add(Calendar.DAY_OF_MONTH, -1);// 往前一天
                }catch (Exception e){
                    logger.error("同步数据异常：",e);
                }
            }
//        });
    }


    @Override
    public void syncBaseDataFromNet() {
        Calendar startCalendar = DateUtils.getYesterdayOfNumber(new Date(),-1);
        Calendar endCalendar = DateUtils.getYesterdayOfNumber(new Date(),-3);
        //移除所有数据
        while(startCalendar.getTime().getTime() > endCalendar.getTime().getTime()){
            List<GameBean> gameBeans;
            String dateStr = DateUtils.dayFormatString(startCalendar.getTime());
            logger.info("网站同步数据当前日期为："+ dateStr);
            try{
                //数据获取
                String json =  HttpClient4.doGet("http://odds.zgzcw.com/odds/oyzs_ajax.action?type=qb&issue="+dateStr+"&date=&companys=14");
                logger.info("网站获取信息为：" + json);
                gameBeans = JSONObject.parseArray(json, GameBean.class);
                if(!CollectionUtils.isEmpty(gameBeans)){
                    for(GameBean gameBean : gameBeans){
                        if(!GameCheckUtils.gameCheck(gameBean.getLEAGUE_NAME_SIMPLY())){
                            continue;
                        }
                        //同步赛事基本信息
                        createBaseGameData(gameBean);
                    }
                }
                startCalendar.add(Calendar.DAY_OF_MONTH, -1);// 往前一天
            }catch (Exception e){
                logger.error("同步数据异常：",e);
            }
        }


    }



    @Override
    @Transactional
    public void syncBigSmallDataFromNet() {
        Date now = new Date();
        Calendar startCalendar = DateUtils.getYesterdayOfNumber(new Date(),0);
        Calendar endCalendar = DateUtils.getYesterdayOfNumber(new Date(),-3);
        while(startCalendar.getTime().getTime() > endCalendar.getTime().getTime()){
            List<GameBean> gameBeans;
            String dateStr = DateUtils.dayFormatString(startCalendar.getTime());
            logger.info("网站同步数据当前日期为："+ dateStr);
            try{
                //数据获取
                String json =  HttpClient4.doGet("http://odds.zgzcw.com/odds/oyzs_ajax.action?type=qb&issue="+dateStr+"&date=&companys=14");
                logger.info("网站获取信息为：" + json);
                gameBeans = JSONObject.parseArray(json, GameBean.class);
                if(!CollectionUtils.isEmpty(gameBeans)){
                    for(GameBean gameBean : gameBeans){
                        if(!GameCheckUtils.gameCheck(gameBean.getLEAGUE_NAME_SIMPLY())){
                            continue;
                        }
                        Date matchDate = DateUtils.str2Date("yyyy-MM-dd HH:mm:ss",gameBean.getMATCH_TIME());
                        //比赛开始三小时前开始的比赛为历史比赛
                        if (now.getTime() - matchDate.getTime() > 3*6*60*60000){
                            //大小球数据
                            createBigSmallHistoryData(gameBean);
                            //同步历史赛事赛事基本信息
                            createBaseGameData(gameBean);
                        }else{
                            //新的大小球数据
                        }


                    }
                }
                startCalendar.add(Calendar.DAY_OF_MONTH, -1);// 往前一天
            }catch (Exception e){
                logger.error("同步数据异常：",e);
            }
        }
    }


    @Override
    @Transactional
    @Scheduled(cron="0 0 4 * * ?")
    public void syncDataFromNetIncrement() {
        Date now = new Date();
        Calendar startCalendar = DateUtils.getYesterdayOfNumber(new Date(),0);
        Calendar endCalendar = DateUtils.getYesterdayOfNumber(new Date(),-4);
        while(startCalendar.getTime().getTime() > endCalendar.getTime().getTime()){
            List<GameBean> gameBeans;
            String dateStr = DateUtils.dayFormatString(startCalendar.getTime());
            logger.info("网站同步数据当前日期为："+ dateStr);
            try{
                //数据获取
                String json =  HttpClient4.doGet("http://odds.zgzcw.com/odds/oyzs_ajax.action?type=qb&issue="+dateStr+"&date=&companys=14");
                logger.info("网站获取信息为：" + json);
                gameBeans = JSONObject.parseArray(json, GameBean.class);
                if(!CollectionUtils.isEmpty(gameBeans)){
                    for(GameBean gameBean : gameBeans){
                        if(!GameCheckUtils.gameCheck(gameBean.getLEAGUE_NAME_SIMPLY())){
                            continue;
                        }
                        Date matchDate = DateUtils.str2Date("yyyy-MM-dd HH:mm:ss",gameBean.getMATCH_TIME());
                        //比赛开始三小时前开始的比赛为历史比赛
                        if (now.getTime() - matchDate.getTime() > 3*60*60000){
                            //大小球数据
                            createBigSmallHistoryData(gameBean);
                            //同步历史赛事赛事基本信息
                            createBaseGameData(gameBean);

                            updateBigSmallData(gameBean);

                        }else{
                            //新的大小球数据
                            createBigSmallNewData(gameBean);
                        }


                    }
                }
                startCalendar.add(Calendar.DAY_OF_MONTH, -1);// 往前一天
            }catch (Exception e){
                logger.error("同步数据异常：",e);
            }
        }
    }




    private void createAsiansHistoryData(GameBean gameBean) throws ParseException {
        AsiansHistoryData asiansHistoryData = new AsiansHistoryData();
        asiansHistoryData.setMatch_id(gameBean.getID());
        asiansHistoryData.setLeague_name_simply(gameBean.getLEAGUE_NAME_SIMPLY());
        asiansHistoryData.setHost_name(gameBean.getHOST_NAME());
        asiansHistoryData.setGuest_name(gameBean.getGUEST_NAME());
        asiansHistoryData.setHost_goal(gameBean.getHOST_GOAL());
        asiansHistoryData.setGuest_goal(gameBean.getGUEST_GOAL());
        if(gameBean.getHOST_GOAL() > gameBean.getGUEST_GOAL()){
            asiansHistoryData.setGame_result(1);
        }else if (gameBean.getGUEST_GOAL() == gameBean.getHOST_GOAL()){
            asiansHistoryData.setGame_result(2);
        }else{
            asiansHistoryData.setGame_result(3);
        }

        asiansHistoryData.setMatch_time(DateUtils.str2Date("yyyy-MM-dd HH:mm:ss",gameBean.getMATCH_TIME()));
        if(!CollectionUtils.isEmpty(gameBean.getListOdds())){
            for(CompanyOdd companyOdd : gameBean.getListOdds()){

                asiansHistoryData.setCompany_name(companyOdd.getCOMPANY_NAME());
                asiansHistoryData.setFirst_let_ball(StringUtils.isBlank(companyOdd.getFIRST_HANDICAP())? null : new BigDecimal(companyOdd.getFIRST_HANDICAP()));
                asiansHistoryData.setFirst_host_bet(StringUtils.isBlank(companyOdd.getFIRST_HOST())? null : new BigDecimal(companyOdd.getFIRST_HOST()));
                asiansHistoryData.setFirst_guest_bet(StringUtils.isBlank(companyOdd.getFIRST_GUEST())? null : new BigDecimal(companyOdd.getFIRST_GUEST()));
                asiansHistoryData.setLet_ball(StringUtils.isBlank(companyOdd.getHANDICAP())? null : new BigDecimal(companyOdd.getHANDICAP()));
                asiansHistoryData.setHost_bet(StringUtils.isBlank(companyOdd.getHOST())? null : new BigDecimal(companyOdd.getHOST()));
                asiansHistoryData.setGuest_bet(StringUtils.isBlank(companyOdd.getGUEST())? null : new BigDecimal(companyOdd.getGUEST()));
                if(asiansHistoryData.getHost_goal() == null
                        || asiansHistoryData.getGuest_goal() == null
                        || asiansHistoryData.getLet_ball() == null){
                    continue;
                }
                BigDecimal asiansResult = new BigDecimal(asiansHistoryData.getHost_goal())
                        .subtract(new BigDecimal(asiansHistoryData.getGuest_goal())).add(asiansHistoryData.getLet_ball());
                if(asiansResult.doubleValue() > 0.3){
                    asiansHistoryData.setBuy_host(new BigDecimal(2));
                    asiansHistoryData.setBuy_guest(new BigDecimal(0));
                }else if(asiansResult.doubleValue() < 0.3 && asiansResult.doubleValue() > 0){
                    asiansHistoryData.setBuy_host(new BigDecimal(1.5));
                    asiansHistoryData.setBuy_guest(new BigDecimal(0.5));
                }else if(asiansResult.doubleValue() == 0){
                    asiansHistoryData.setBuy_host(new BigDecimal(1));
                    asiansHistoryData.setBuy_guest(new BigDecimal(1));
                }else if(asiansResult.doubleValue() < 0&& asiansResult.doubleValue() > -0.3){
                    asiansHistoryData.setBuy_host(new BigDecimal(0.5));
                    asiansHistoryData.setBuy_guest(new BigDecimal(1.5));
                }else if(asiansResult.doubleValue() < -0.3 ){
                    asiansHistoryData.setBuy_host(new BigDecimal(0));
                    asiansHistoryData.setBuy_guest(new BigDecimal(2));
                }
                asiansDao.insertAsiansHistoryData(asiansHistoryData);
            }
        }
    }


    private void createBigSmallHistoryData(GameBean gameBean) throws ParseException {
        BigSmallData bigSmallData = new BigSmallData();
        bigSmallData.setMatch_id(gameBean.getID());
        bigSmallData.setLeague_name_simply(gameBean.getLEAGUE_NAME_SIMPLY());
        bigSmallData.setHost_name(gameBean.getHOST_NAME());
        bigSmallData.setGuest_name(gameBean.getGUEST_NAME());
        bigSmallData.setHost_goal(gameBean.getHOST_GOAL());
        bigSmallData.setGuest_goal(gameBean.getGUEST_GOAL());
        if(gameBean.getHOST_GOAL() > gameBean.getGUEST_GOAL()){
            bigSmallData.setGame_result(1);
        }else if (gameBean.getGUEST_GOAL() == gameBean.getHOST_GOAL()){
            bigSmallData.setGame_result(2);
        }else{
            bigSmallData.setGame_result(3);
        }
        bigSmallData.setTotal_goal(gameBean.getHOST_GOAL() + gameBean.getGUEST_GOAL());
        bigSmallData.setMatch_time(DateUtils.str2Date("yyyy-MM-dd HH:mm:ss",gameBean.getMATCH_TIME()));
        if(!CollectionUtils.isEmpty(gameBean.getListOdds())){
            for(CompanyOdd companyOdd : gameBean.getListOdds()){
                bigSmallData.setCompany_name(companyOdd.getCOMPANY_NAME());
                bigSmallData.setFirst_let_big_small(StringUtils.isBlank(companyOdd.getDW_FIRST_HANDICAP())? null : new BigDecimal(companyOdd.getDW_FIRST_HANDICAP()));
                bigSmallData.setFirst_big(StringUtils.isBlank(companyOdd.getFIRST_BIG())? null : new BigDecimal(companyOdd.getFIRST_BIG()));
                bigSmallData.setFirst_small(StringUtils.isBlank(companyOdd.getFIRST_SMALL())? null : new BigDecimal(companyOdd.getFIRST_SMALL()));
                bigSmallData.setLet_big_small(StringUtils.isBlank(companyOdd.getDW_HANDICAP())? null : new BigDecimal(companyOdd.getDW_HANDICAP()));
                bigSmallData.setBig(StringUtils.isBlank(companyOdd.getBIG())? null : new BigDecimal(companyOdd.getBIG()));
                bigSmallData.setSmall(StringUtils.isBlank(companyOdd.getSMALL())? null : new BigDecimal(companyOdd.getSMALL()));
                if(bigSmallData.getHost_goal() == null
                        || bigSmallData.getGuest_goal() == null
                        || bigSmallData.getFirst_let_big_small() == null){
                    continue;
                }
                BigDecimal bigSmallResult = new BigDecimal(bigSmallData.getTotal_goal())
//                        .subtract(bigSmallData.getLet_big_small());
                        .subtract(bigSmallData.getFirst_let_big_small());
                if(bigSmallResult.doubleValue() > 0.3){
                    bigSmallData.setBuy_big(new BigDecimal(1.8));
                    bigSmallData.setBuy_small(new BigDecimal(0));
                }else if(bigSmallResult.doubleValue() < 0.3 && bigSmallResult.doubleValue() > 0){
                    bigSmallData.setBuy_big(new BigDecimal(1.4));
                    bigSmallData.setBuy_small(new BigDecimal(0.5));
                }else if(bigSmallResult.doubleValue() == 0){
                    bigSmallData.setBuy_big(new BigDecimal(1));
                    bigSmallData.setBuy_small(new BigDecimal(1));
                }else if(bigSmallResult.doubleValue() < 0&& bigSmallResult.doubleValue() > -0.3){
                    bigSmallData.setBuy_big(new BigDecimal(0.5));
                    bigSmallData.setBuy_small(new BigDecimal(1.4));
                }else if(bigSmallResult.doubleValue() < -0.3 ){
                    bigSmallData.setBuy_big(new BigDecimal(0));
                    bigSmallData.setBuy_small(new BigDecimal(1.8));
                }
                bigSmallDao.deleteBigSmallHistoryDataBy(bigSmallData);
                bigSmallDao.insertBigSmallHistoryData(bigSmallData);
            }
        }
    }

    private void createBigSmallNewData(GameBean gameBean) throws ParseException {
        BigSmallData bigSmallData = new BigSmallData();
        bigSmallData.setMatch_id(gameBean.getID());
        bigSmallData.setLeague_name_simply(gameBean.getLEAGUE_NAME_SIMPLY());
        bigSmallData.setHost_name(gameBean.getHOST_NAME());
        bigSmallData.setGuest_name(gameBean.getGUEST_NAME());
        bigSmallData.setTotal_goal(gameBean.getHOST_GOAL() + gameBean.getGUEST_GOAL());
        bigSmallData.setMatch_time(DateUtils.str2Date("yyyy-MM-dd HH:mm:ss",gameBean.getMATCH_TIME()));
        if(!CollectionUtils.isEmpty(gameBean.getListOdds())){
            for(CompanyOdd companyOdd : gameBean.getListOdds()){
                bigSmallData.setCompany_name(companyOdd.getCOMPANY_NAME());
                bigSmallData.setFirst_let_big_small(StringUtils.isBlank(companyOdd.getDW_FIRST_HANDICAP())? null : new BigDecimal(companyOdd.getDW_FIRST_HANDICAP()));
                bigSmallData.setFirst_big(StringUtils.isBlank(companyOdd.getFIRST_BIG())? null : new BigDecimal(companyOdd.getFIRST_BIG()));
                bigSmallData.setFirst_small(StringUtils.isBlank(companyOdd.getFIRST_SMALL())? null : new BigDecimal(companyOdd.getFIRST_SMALL()));
                bigSmallData.setLet_big_small(StringUtils.isBlank(companyOdd.getDW_HANDICAP())? null : new BigDecimal(companyOdd.getDW_HANDICAP()));
                bigSmallData.setBig(StringUtils.isBlank(companyOdd.getBIG())? null : new BigDecimal(companyOdd.getBIG()));
                bigSmallData.setSmall(StringUtils.isBlank(companyOdd.getSMALL())? null : new BigDecimal(companyOdd.getSMALL()));
                if( bigSmallData.getFirst_let_big_small() == null){
                    continue;
                }
                //新表中获取数据
                BigSmallData newBigSmallData = bigSmallDao.getBigSmallDataByMatchId(bigSmallData.getMatch_id());
                if(newBigSmallData != null && newBigSmallData.getFirst_let_big_small() != null ){
                    return;
                }
                bigSmallDao.deleteBigSmallNewDataBy(bigSmallData);
                bigSmallDao.insertBigSmallData(bigSmallData);
            }
        }
    }


    private void updateBigSmallData(GameBean gameBean) throws ParseException {
        BigSmallData bigSmallData = new BigSmallData();
        bigSmallData.setMatch_id(gameBean.getID());
        bigSmallData.setLeague_name_simply(gameBean.getLEAGUE_NAME_SIMPLY());
        bigSmallData.setHost_name(gameBean.getHOST_NAME());
        bigSmallData.setGuest_name(gameBean.getGUEST_NAME());
        bigSmallData.setHost_goal(gameBean.getHOST_GOAL());
        bigSmallData.setGuest_goal(gameBean.getGUEST_GOAL());
        if(gameBean.getHOST_GOAL() > gameBean.getGUEST_GOAL()){
            bigSmallData.setGame_result(1);
        }else if (gameBean.getGUEST_GOAL() == gameBean.getHOST_GOAL()){
            bigSmallData.setGame_result(2);
        }else{
            bigSmallData.setGame_result(3);
        }
        bigSmallData.setTotal_goal(gameBean.getHOST_GOAL() + gameBean.getGUEST_GOAL());
        bigSmallData.setMatch_time(DateUtils.str2Date("yyyy-MM-dd HH:mm:ss",gameBean.getMATCH_TIME()));
        if(!CollectionUtils.isEmpty(gameBean.getListOdds())){
            for(CompanyOdd companyOdd : gameBean.getListOdds()){
                bigSmallData.setCompany_name(companyOdd.getCOMPANY_NAME());
                bigSmallData.setFirst_let_big_small(StringUtils.isBlank(companyOdd.getDW_FIRST_HANDICAP())? null : new BigDecimal(companyOdd.getDW_FIRST_HANDICAP()));
                bigSmallData.setFirst_big(StringUtils.isBlank(companyOdd.getFIRST_BIG())? null : new BigDecimal(companyOdd.getFIRST_BIG()));
                bigSmallData.setFirst_small(StringUtils.isBlank(companyOdd.getFIRST_SMALL())? null : new BigDecimal(companyOdd.getFIRST_SMALL()));
                bigSmallData.setLet_big_small(StringUtils.isBlank(companyOdd.getDW_HANDICAP())? null : new BigDecimal(companyOdd.getDW_HANDICAP()));
                bigSmallData.setBig(StringUtils.isBlank(companyOdd.getBIG())? null : new BigDecimal(companyOdd.getBIG()));
                bigSmallData.setSmall(StringUtils.isBlank(companyOdd.getSMALL())? null : new BigDecimal(companyOdd.getSMALL()));
                if(bigSmallData.getHost_goal() == null
                        || bigSmallData.getGuest_goal() == null
                        || bigSmallData.getFirst_let_big_small() == null){
                    continue;
                }
                BigDecimal bigSmallResult = new BigDecimal(bigSmallData.getTotal_goal())
//                        .subtract(bigSmallData.getLet_big_small());
                        .subtract(bigSmallData.getFirst_let_big_small());
                if(bigSmallResult.doubleValue() > 0.3){
                    bigSmallData.setBuy_big(new BigDecimal(1.8));
                    bigSmallData.setBuy_small(new BigDecimal(0));
                }else if(bigSmallResult.doubleValue() < 0.3 && bigSmallResult.doubleValue() > 0){
                    bigSmallData.setBuy_big(new BigDecimal(1.4));
                    bigSmallData.setBuy_small(new BigDecimal(0.5));
                }else if(bigSmallResult.doubleValue() == 0){
                    bigSmallData.setBuy_big(new BigDecimal(1));
                    bigSmallData.setBuy_small(new BigDecimal(1));
                }else if(bigSmallResult.doubleValue() < 0&& bigSmallResult.doubleValue() > -0.3){
                    bigSmallData.setBuy_big(new BigDecimal(0.5));
                    bigSmallData.setBuy_small(new BigDecimal(1.4));
                }else if(bigSmallResult.doubleValue() < -0.3 ){
                    bigSmallData.setBuy_big(new BigDecimal(0));
                    bigSmallData.setBuy_small(new BigDecimal(1.8));
                }
                //新表中获取数据
                BigSmallData newBigSmallData = bigSmallDao.getBigSmallDataByMatchId(bigSmallData.getMatch_id());
                if(newBigSmallData == null){
                    return;
                }
                BigSmallData update = new BigSmallData();
                update.setMatch_id(bigSmallData.getMatch_id());
                update.setHost_goal(bigSmallData.getHost_goal());
                update.setGuest_goal(bigSmallData.getGuest_goal());
                update.setGame_result(bigSmallData.getGame_result());
                update.setBuy_big(bigSmallData.getBuy_big());
                update.setBuy_small(bigSmallData.getBuy_small());
                if(newBigSmallData.getBig_small_pre() != null){
                    if(newBigSmallData.getBig_small_pre() == 1 ){
                        update.setBuy_result_real(bigSmallData.getBuy_big());
                    }else if(newBigSmallData.getBig_small_pre() == 2){
                        update.setBuy_result_real(bigSmallData.getBuy_small());
                    }
                    update.setBuy_result_real(update.getBuy_result_real().subtract(new BigDecimal(1)));
                }
                update.setIs_end(1);
                bigSmallDao.updateBigSmallData(update);
            }
        }
    }



    private void createEuropeHistoryData(GameBean gameBean) throws ParseException {
        EuropeHistoryData europeHistoryData = new EuropeHistoryData();
        europeHistoryData.setMatch_id(gameBean.getID());
        europeHistoryData.setLeague_name_simply(gameBean.getLEAGUE_NAME_SIMPLY());
        europeHistoryData.setHost_name(gameBean.getHOST_NAME());
        europeHistoryData.setGuest_name(gameBean.getGUEST_NAME());
        europeHistoryData.setHost_goal(gameBean.getHOST_GOAL());
        europeHistoryData.setGuest_goal(gameBean.getGUEST_GOAL());
        if(gameBean.getHOST_GOAL() > gameBean.getGUEST_GOAL()){
            europeHistoryData.setGame_result(1);
        }else if (gameBean.getGUEST_GOAL() == gameBean.getHOST_GOAL()){
            europeHistoryData.setGame_result(2);
        }else{
            europeHistoryData.setGame_result(3);
        }
        europeHistoryData.setMatch_time(DateUtils.str2Date("yyyy-MM-dd HH:mm:ss",gameBean.getMATCH_TIME()));
        if(!CollectionUtils.isEmpty(gameBean.getListOdds())){
            for(CompanyOdd companyOdd : gameBean.getListOdds()){
                europeHistoryData.setCompany_name(companyOdd.getCOMPANY_NAME());
                europeHistoryData.setFirst_win(StringUtils.isBlank(companyOdd.getFIRST_WIN())? null : new BigDecimal(companyOdd.getFIRST_WIN()));
                europeHistoryData.setFirst_same(StringUtils.isBlank(companyOdd.getFIRST_SAME())? null : new BigDecimal(companyOdd.getFIRST_SAME()));
                europeHistoryData.setFirst_lost(StringUtils.isBlank(companyOdd.getFIRST_LOST())? null : new BigDecimal(companyOdd.getFIRST_LOST()));
                europeHistoryData.setWin(StringUtils.isBlank(companyOdd.getWIN())? null : new BigDecimal(companyOdd.getWIN()));
                europeHistoryData.setSame(StringUtils.isBlank(companyOdd.getSAME())? null : new BigDecimal(companyOdd.getSAME()));
                europeHistoryData.setLost(StringUtils.isBlank(companyOdd.getLOST())? null : new BigDecimal(companyOdd.getLOST()));
                if(europeHistoryData.getGame_result() == 1){
                    europeHistoryData.setBuy_win(new BigDecimal(2));
                }else if(europeHistoryData.getGame_result() == 2){
                    europeHistoryData.setBuy_same(new BigDecimal(2));
                }else if(europeHistoryData.getGame_result() == 3){
                    europeHistoryData.setBuy_lost(new BigDecimal(2));
                }
                europeDao.insertEuropeHistoryData(europeHistoryData);
            }
        }
    }


    private void createBaseGameData(GameBean gameBean) throws ParseException {
        GameBaseData gameBaseData = new GameBaseData();
        gameBaseData.setMatch_id(gameBean.getID());
        gameBaseData.setLeague_name_simply(gameBean.getLEAGUE_NAME_SIMPLY());
        gameBaseData.setHost_name(gameBean.getHOST_NAME());
        gameBaseData.setGuest_name(gameBean.getGUEST_NAME());
        gameBaseData.setHost_goal(gameBean.getHOST_GOAL());
        gameBaseData.setGuest_goal(gameBean.getGUEST_GOAL());
        if(gameBean.getHOST_GOAL() > gameBean.getGUEST_GOAL()){
            gameBaseData.setGame_result(1);
        }else if (gameBean.getGUEST_GOAL() == gameBean.getHOST_GOAL()){
            gameBaseData.setGame_result(2);
        }else{
            gameBaseData.setGame_result(3);
        }
        gameBaseData.setMatch_time(DateUtils.str2Date("yyyy-MM-dd HH:mm:ss",gameBean.getMATCH_TIME()));
        if(gameBaseData.getMatch_time().getTime() + 3*60*60000 > new Date().getTime()){
            return;
        }
        gameBaseDao.deleteGameBaseData(gameBaseData);
        gameBaseDao.insertGameBaseData(gameBaseData);

    }


    @Override
    @Transactional
    public void syncRankData() throws IOException {
        Map<Integer,String> map =  GameCheckUtils.getLeagueName();
        Set<Integer> leagueIds = map.keySet();
        for(Integer leagueId : leagueIds){
            Document doc = Jsoup.connect("http://saishi.zgzcw.com/soccer/league/" + leagueId).get();
            String title = doc.title();
            Elements trs = doc.getElementById("tabs1_main_1").select("tr");
            for(int i = 0;i<trs.size();i++){
                Elements tds = trs.get(i).select("td");
                if(tds.size() > 0){
                    TeamRankingData teamRankingData = new TeamRankingData();
                    teamRankingData.setRemark(title);
                    teamRankingData.setLeague_name_simply(map.get(leagueId));
                    teamRankingData.setTeam_name(tds.get(1).text());
                    teamRankingData.setRank(Integer.parseInt(tds.get(0).text()));
                    teamRankingData.setTotal_game(Integer.parseInt(tds.get(2).text()));
                    teamRankingData.setWin_game(Integer.parseInt(tds.get(3).text()));
                    teamRankingData.setSame_game(Integer.parseInt(tds.get(4).text()));
                    teamRankingData.setLost_game(Integer.parseInt(tds.get(5).text()));
                    System.out.println(teamRankingData);
                    rankingDao.insertTeamRankingData(teamRankingData);

                }

            }

        }
    }

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("http://saishi.zgzcw.com/soccer/league/36").get();
        String title = doc.title();
        Elements trs = doc.getElementById("tabs1_main_1").select("tr");
        for(int i = 0;i<trs.size();i++){
            Elements tds = trs.get(i).select("td");
            if(tds.size() > 0){
                TeamRankingData teamRankingData = new TeamRankingData();
                teamRankingData.setLeague_name_simply("英超");
                teamRankingData.setTeam_name(tds.get(1).text());
                teamRankingData.setRank(Integer.parseInt(tds.get(0).text()));
                teamRankingData.setTotal_game(Integer.parseInt(tds.get(2).text()));
                teamRankingData.setWin_game(Integer.parseInt(tds.get(3).text()));
                teamRankingData.setSame_game(Integer.parseInt(tds.get(4).text()));
                teamRankingData.setLost_game(Integer.parseInt(tds.get(5).text()));
            }

        }
        System.out.println(title);
    }




}
