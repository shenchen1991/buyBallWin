package com.shenchen.test.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @program: buyBallWin
 * @description: NIO服务端
 * @author: 沈陈
 * @create: 2019-12-31 10:26
 **/
public class NIOServer {
    public static void main(String[] args) throws IOException {

        // 1.serverSelector负责轮询是否有新的连接，服务端检测到新的连接之后，不再创建新的线程，
        // 而是直接将新连接绑定到clientSelector上，这样就不用IO模型中1W个while循环等待
        Selector serverSelector = Selector.open();

        // 2.clientSelector负责轮询连接是否有数据可读
        Selector clientSelector = Selector.open();

        new Thread(() -> {
            try {
                // 对应IO编程中服务端启动
                ServerSocketChannel listenerChannel = ServerSocketChannel.open();
                listenerChannel.socket().bind(new InetSocketAddress(3333));
                listenerChannel.configureBlocking(false);
                listenerChannel.register(serverSelector, SelectionKey.OP_ACCEPT);


                while (true){
                    // 检测是否有新的连接，这里的1是阻塞的会见为1ms
                    if(serverSelector.select(1) > 0){
                        Set<SelectionKey> set = serverSelector.selectedKeys();
                        Iterator<SelectionKey> keyIterable = set.iterator();

                        while(keyIterable.hasNext()){
                            SelectionKey key = keyIterable.next();

                            if(key.isAcceptable()){
                                try{
                                    // (1) 每来一个新连接，不需要创建一个线程，而是直接注册到clientSelector
                                    SocketChannel clientChannel = ((ServerSocketChannel)key.channel()).accept();
                                    clientChannel.configureBlocking(false);
                                    clientChannel.register(clientSelector,SelectionKey.OP_READ);
                                }finally {
                                    keyIterable.remove();
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        new Thread(() ->{

                try {

                    // (2) 批量轮询是否有哪些连接有数据可读，1 为阻塞时间
                    while (true){
                        if(clientSelector.select(1) > 0){
                            Set<SelectionKey> set = clientSelector.selectedKeys();
                            Iterator<SelectionKey> keyIterator = set.iterator();
                            while (keyIterator.hasNext()){
                                SelectionKey key = keyIterator.next();

                                if(key.isReadable()){
                                    try{
                                        SocketChannel clientChannel = (SocketChannel) key.channel();
                                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                                        // (3) 面向buffer
                                        clientChannel.read(byteBuffer);
                                        byteBuffer.flip();
                                        System.out.println(Charset.defaultCharset().newDecoder().decode(byteBuffer).toString());

                                    }finally {
                                        keyIterator.remove();
                                        key.interestOps(SelectionKey.OP_READ);
                                    }
                                }
                            }

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }).start();


    }
}
