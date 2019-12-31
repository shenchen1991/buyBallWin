package com.shenchen.util;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;

/**
 * @program: buyBallWin
 * @description:
 * @author: 沈陈
 * @create: 2019-11-29 17:38
 **/
public class CatTest {

    public static void main(String[] args) {Transaction t = Cat.newTransaction("some_type", "some_name");  //传入 type 和 name
        try {
            // do something here
            double a=1/0;
            t.addData("some detail");
            t.setStatus(Transaction.SUCCESS);
        } catch (Exception e) {
            Cat.logError(e);
            t.setStatus(e);
        } finally {
            t.complete();
        }
    }
}
