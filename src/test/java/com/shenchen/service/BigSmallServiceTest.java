package com.shenchen.service;

import com.shenchen.TestSupport;
import com.shenchen.model.BigSmallResultData;
import com.shenchen.model.BigSmallResultDatas;
import com.shenchen.util.GameCheckUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BigSmallServiceTest extends TestSupport {

    @Autowired
    IBigSmallService bigSmallService;

   @Test
    public void analyseBigSmallTest(){
       bigSmallService.analyseBigSmall();
   }

    @Test
    public void analyseBigSmallTest2(){
        List<Map<String, Object>> result = bigSmallService.analyseBigSmall("英甲");
        for (Map<String, Object> map : result){
            Set<String> keys = map.keySet();
            for(String key : keys){
                System.out.print(key + "=" + map.get(key) + "   ");
            }
            System.out.println();

        }
    }

    @Test
    public void analyseBigSmallEfficientTest2(){
       List<BigSmallResultDatas> resultDatasList = new ArrayList<>();
        List<String> gameNames = GameCheckUtils.gameList();
       for(String gameName : gameNames){
           BigSmallResultDatas bigSmallResultDatas = new BigSmallResultDatas();
           BigSmallResultData maxData = null;
           BigSmallResultData minData = null;
           List<BigSmallResultData> result = bigSmallService.analyseBigSmallEfficient(gameName,true);
           for (BigSmallResultData data : result){
               if(maxData == null){
                   maxData = data;
                   minData = data;
               }
               if(data.getWinRate() > maxData.getWinRate()){
                   maxData = data;
               }
               if(data.getWinRate() < minData.getWinRate()){
                   minData = data;
               }
               System.out.println(data);
           }bigSmallResultDatas.setLeague_name_simply(gameName);
           bigSmallResultDatas.setMaxData(maxData);
           bigSmallResultDatas.setMinData(minData);
           resultDatasList.add(bigSmallResultDatas);
       }
       for(BigSmallResultDatas bigSmallResultDatas : resultDatasList){
           System.out.println("============="+bigSmallResultDatas.getLeague_name_simply()+"===================");
           System.out.println(bigSmallResultDatas.getMaxData());
           System.out.println(bigSmallResultDatas.getMinData());
           System.out.println("================================================================================");
       }



    }

    @Test
    public void analyseBigSmallTest4(){
        List<Map<String, Object>> result = bigSmallService.analyseBigSmall("英乙");
        for (Map<String, Object> map : result){
            Set<String> keys = map.keySet();
            for(String key : keys){
                System.out.print(key + "=" + map.get(key) + "   ");
            }
            System.out.println();

        }
    }

    @Test
    public void analyseBigSmallTest5(){
        List<Map<String, Object>> result = bigSmallService.analyseBigSmall("意甲");
        for (Map<String, Object> map : result){
            Set<String> keys = map.keySet();
            for(String key : keys){
                System.out.print(key + "=" + map.get(key) + "   ");
            }
            System.out.println();

        }
    }

    @Test
    public void analyseBigSmallTest3(){
        bigSmallService.analyseBigSmall("阿甲",false,0.15D,0.85D,0.50D,0.50D);//0.53846
//        bigSmallService.analyseBigSmall("阿乙",false,1D,0D,0.40D,0.60D);
//        bigSmallService.analyseBigSmall("波兰超",false,1D,0D,0.40D,0.60D);


        bigSmallService.analyseBigSmall("英超",true,1D,0D,0.40D,0.60D);//0.55797
        bigSmallService.analyseBigSmall("英冠",false,0.75D,0.25D,0.15D,0.85D);//0.6
        bigSmallService.analyseBigSmall("英甲",false,0.95D,0.05D,1D,0D);//0.55963
        bigSmallService.analyseBigSmall("英乙",true,1D,0D,0.9D,0.1D);//0.57062
        bigSmallService.analyseBigSmall("意甲",true,0.65D,0.35D,0.80D,0.20D);//0.58278

        bigSmallService.analyseBigSmall("意乙",false,0.55D,0.45D,0.15D,0.85D);//0.60163
        bigSmallService.analyseBigSmall("西甲",false,0.60D,0.40D,0.05D,0.95D);//0.6
//        bigSmallService.analyseBigSmall("西乙",true,0.60D,0.40D,0.05D,0.95D);
        bigSmallService.analyseBigSmall("德甲",false,0.75D,0.25D,0.05D,0.95D);//0.55
        bigSmallService.analyseBigSmall("德乙",true,0.01D,0.90D,1D,0D);//0.56481
        bigSmallService.analyseBigSmall("法甲",false,0.75D,0.25D,1D,0D);//0.57241
//        bigSmallService.analyseBigSmall("法乙",false,0.75D,0.25D,1D,0D);
        bigSmallService.analyseBigSmall("荷甲",false,0.70D,0.30D,0.50D,0.50D);//0.61607
//        bigSmallService.analyseBigSmall("荷乙",false,0.70D,0.30D,0.50D,0.50D);
//        bigSmallService.analyseBigSmall("挪超",false,0.05D,0.95D,0.60D,0.40D);//0.63636
        bigSmallService.analyseBigSmall("葡超",false,0.85D,0.15D,0.05D,0.95D);//0.56897

        bigSmallService.analyseBigSmall("葡甲",false,1D,0D,0.85D,0.15D);//0.58824
        bigSmallService.analyseBigSmall("苏超",false,0.20D,0.80D,0.85D,0.15D);//0.55844
        bigSmallService.analyseBigSmall("保超",false,0.05D,0.95D,0.15D,0.85D);//0.64706

//        bigSmallService.analyseBigSmall("以超",false,0.05D,0.95D,0.15D,0.85D);
        bigSmallService.analyseBigSmall("土超",false,1D,0D,0.55D,0.45D);//0.53061
        bigSmallService.analyseBigSmall("比甲",false,0.85,0.15D,0.05D,0.95D);//0.50943

//        bigSmallService.analyseBigSmall("比乙",false,1D,0D,0.80D,0.2D);//0.70833
        bigSmallService.analyseBigSmall("瑞士超",false,0.50D,0.50D,0.95D,0.05D);//0.60714
        bigSmallService.analyseBigSmall("捷甲",true,0.10D,0.90D,0.70D,0.30D);//0.60825
        bigSmallService.analyseBigSmall("罗甲",false,0.10D,0.90D,0.4D,0.6D);//0.64286
        bigSmallService.analyseBigSmall("土甲",false,0.20D,0.80D,0.5D,0.5D);//0.6

    }
}
