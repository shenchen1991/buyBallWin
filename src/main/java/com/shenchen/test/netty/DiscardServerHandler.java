package com.shenchen.test.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @program: buyBallWin
 * @description: 处理服务端的Channel
 * @author: 沈陈
 * @create: 2019-12-31 17:17
 **/
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        //丢弃掉收到的数据
        ((ByteBuf)msg).release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
