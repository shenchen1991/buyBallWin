package com.shenchen.test.io;

import java.net.Socket;
import java.util.Date;

/**
 * @program: buyBallWin
 * @description: BIO客户端
 * @author: 沈陈
 * @create: 2019-12-30 13:33
 **/
public class IOClient {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1",3333);
                while (true){
                    socket.getOutputStream().write((new Date() + "test").getBytes());
                    Thread.sleep(2000);
                }
            }catch (Exception e){
            }
        }).start();
    }
}
