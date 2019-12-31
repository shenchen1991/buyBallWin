package com.shenchen.util;

import java.util.*;

public class Permutation {
    public static int total = 0;
    public static List<Integer[]> arrays = new ArrayList<>();

    public static Set<Integer> everyTotalSet = new HashSet<>();
    public static Integer big = 0;
    public static Integer small = 0;
    public static void swap(Integer[] str, int i, int j)
    {
        Integer temp;
        temp = str[i];
        str[i] = str[j];
        str[j] = temp;
    }
    public static void arrange (Integer[] str, int st, int len)
    {
        if (st == len - 1)
        {
            Integer everyTotal = 0;
            for (int i = 0; i < len; i ++)
            {

                System.out.print(str[i]+ "  ");
                everyTotal = everyTotal + str[i];
            }

            arrays.add(Arrays.asList(str).toArray(new Integer[5]));
            everyTotalSet.add(everyTotal);
            if(everyTotal%10 > 4){
                big++;
            }else{
                small++;
            }

            System.out.println();
            total++;
        }
        else
        {
            for (int i = st; i < len; i ++)
            {
                swap(str, st, i);
                arrange(str, st + 1, len);
                swap(str, st, i);
            }
        }

    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Integer str[] = {1,2,3,4,5};
        arrange(str, 0, str.length);
        System.out.println(total);
    }
}

