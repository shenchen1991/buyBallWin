package com.shenchen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.shenchen.model.*;
import com.shenchen.service.IAnalyseService;
import com.shenchen.service.IAsiansService;
import com.shenchen.service.IRankingService;
import com.shenchen.util.DateUtils;
import com.shenchen.util.GameCheckUtils;
import com.shenchen.util.HttpClient4;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service("analyseService")
public class AnalyseServiceImpl implements IAnalyseService {

    protected final static Logger logger = Logger.getLogger(AnalyseServiceImpl.class);

    @Autowired
    IAsiansService asiansService;

    @Autowired
    IRankingService rankingService;

    @Override
    public List<GameAnalyseResult> analyseGame() {
        List<GameAnalyseResult> gameAnalyseResults = new ArrayList<>();
//        singleThreadExecutor.execute(() -> {
            Calendar startCalendar = DateUtils.getYesterdayOfNumber(new Date(),0);
            Calendar endCalendar = DateUtils.getYesterdayOfNumber(new Date(),-2);
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
                            //比赛信息
                            GameAnalyseResult gameAnalyseResult = new GameAnalyseResult();
                            gameAnalyseResult.setGameBean(gameBean);
                            AnalyseQuery analyseQuery = new AnalyseQuery();
                            analyseQuery.setLeague_name_simply(gameBean.getLEAGUE_NAME_SIMPLY());
                            analyseQuery.setHost_name(gameBean.getHOST_NAME());
                            analyseQuery.setGuest_name(gameBean.getGUEST_NAME());
                            //名次信息
                            TeamRankingData hostTeamRankingData = rankingService.getHostTeamRanking(analyseQuery);
                            gameAnalyseResult.setHostTeamRanking(hostTeamRankingData);
                            TeamRankingData guestTeamRankingData = rankingService.getGuestTeamRanking(analyseQuery);
                            gameAnalyseResult.setGuestTeamRanking(guestTeamRankingData);
                            //队伍历史信息
                            AsiansHostRateResult asiansHostRateResult = asiansService.getHostAsiansRateResult(analyseQuery);
                            gameAnalyseResult.setAsiansHostRateResult(asiansHostRateResult);

                            AsiansGuestRateResult asiansGuestRateResult = asiansService.getGuestAsiansRateResult(analyseQuery);
                            gameAnalyseResult.setAsiansGuestRateResult(asiansGuestRateResult);
                            gameAnalyseResults.add(gameAnalyseResult);
                        }
                    }
                    startCalendar.add(Calendar.DAY_OF_MONTH, -1);// 往前一天
                }catch (Exception e){
                    logger.error("分析数据异常：",e);
                }
            }
            return gameAnalyseResults;
    }





}
