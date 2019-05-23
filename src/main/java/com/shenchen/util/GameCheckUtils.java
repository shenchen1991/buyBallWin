package com.shenchen.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameCheckUtils implements Serializable {




    public static boolean gameCheck(String league_name_simply){
        String[] strings = {
                "英超","英冠","英甲","英乙",
                "意甲","意乙",
                "西甲","西乙",
                "德甲", "德乙",
                "法甲", "法乙",
                "荷甲", "荷乙",
                "挪超",
                "葡超","葡甲",
                "苏超",
                "保超",
                "以超",
                "土超",
                "比甲","比乙",
                "瑞士超",
                "捷甲",
                "罗甲",
                "土甲",
                "阿甲","阿乙",
                "波兰超",
                "澳洲甲"

        };
        return Arrays.asList(strings).contains(league_name_simply);
    }


    public static List<String> gameList(){
        String[] strings = {

                "阿甲","阿乙",
                "波兰超",
                "澳洲甲",
//                "英超","英冠","英甲","英乙",
//                "意甲","意乙",
//                "西甲","西乙",
//                "德甲", "德乙",
//                "法甲", "法乙",
//                "荷甲", "荷乙",
//                "挪超",
//                "葡超","葡甲",
//                "苏超",
//                "保超",
//                "以超",
//                "土超",
//                "比甲","比乙",
//                "瑞士超",
//                "捷甲",
//                "罗甲",
//                "土甲",


        };
        return Arrays.asList(strings);
    }



    public static Map<Integer,String> getLeagueName(){

        Map<Integer,String> map = new HashMap<>();
        map.put(36,"英超"); map.put(37,"英冠"); map.put(39,"英甲");

        map.put(34,"意甲"); map.put(40,"意乙");

        map.put(8,"德甲"); map.put(9,"德乙");

        map.put(31,"西甲"); map.put(33,"西乙");

        map.put(11,"法甲");map.put(12,"法乙");

        map.put(16,"荷甲");map.put(17,"荷乙");

        map.put(22,"挪超");

        map.put(23,"葡超");map.put(157,"葡甲");

        map.put(29,"苏超");

        return map;
    }


}
