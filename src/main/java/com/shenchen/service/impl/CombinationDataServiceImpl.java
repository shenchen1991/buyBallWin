package com.shenchen.service.impl;

import com.shenchen.dao.ICombinationDataDao;
import com.shenchen.model.CombinationData;
import com.shenchen.service.ICombinationDataService;
import com.shenchen.util.Combination;
import com.shenchen.util.Permutation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("combinationDataService")
public class CombinationDataServiceImpl implements ICombinationDataService {

    @Autowired
    ICombinationDataDao combinationDataDao;

    public void insertCombinationData(){
        List<List<Integer>> resultList = Combination.getCombinationList(11, 5);
        System.out.println(resultList.size());
        for (List<Integer> list : resultList) {
            Permutation.arrange(list.toArray(new Integer[list.size()]), 0, list.size());
        }
        System.out.println(Permutation.arrays.size());
        for (Integer[] integers : Permutation.arrays){
            CombinationData combinationData = new CombinationData();
            combinationData.setNumberOne(integers[0]);
            combinationData.setNumberTwo(integers[1]);
            combinationData.setNumberThree(integers[2]);
            combinationData.setNumberFour(integers[3]);
            combinationData.setNumberFive(integers[4]);
            combinationData.setNumberTotal(integers[0] + integers[1]+ integers[2]+ integers[3]+ integers[4]);
            combinationData.setNumberTotalDouble(combinationData.getNumberTotal()%2);
            combinationData.setNumberEnd(combinationData.getNumberTotal()%10);
            combinationData.setNumberEndDouble(combinationData.getNumberEnd()%2);
            combinationDataDao.insertCombinationData(combinationData);
        }
    }
}