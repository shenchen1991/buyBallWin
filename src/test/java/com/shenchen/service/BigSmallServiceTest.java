package com.shenchen.service;

import com.shenchen.TestSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        List<Map<String, Object>> result = bigSmallService.analyseBigSmall("英冠");
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
        bigSmallService.analyseBigSmall("荷甲",0.70D,0.30D,0.55D,0.45D);
        bigSmallService.analyseBigSmall("英冠",0.75D,0.25D,0.15D,0.85D);

    }
}
