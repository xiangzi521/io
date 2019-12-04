package com.mxz.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description: nio
 * @Author: struggle
 * @CreateDate: 2019/11/28 23:24
 * @Version: 1.0
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        //创建serverSocketChannel
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        //设置channel为非非阻塞方式
        serverChannel.configureBlocking(false);
        //绑定本机服务端口号，
        serverChannel.bind(new InetSocketAddress(8888));
        System.out.println("nio starting :" + serverChannel.getLocalAddress());
        //创建selector
        Selector selector = Selector.open();
        //把当前的通道，建立连接事件，注册在seletor上
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        //创建缓冲区接收数据
        ByteBuffer buffer = ByteBuffer.allocate(2048);

        while (true) {
            int select = selector.select();
            if (select == 0) {
                continue;
            }

            //有通道已经准备好相关事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //循环所有通道上的事件
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            if (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //如果是通道的连接事件，则建立连接好的通道，并将该通道的读事件注册在seletor上
                if (key.isAcceptable()) {
                    //基于socketChannel连接事件触发后，
                    //socket再一次进行封装
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    //创建新的socketChannel
                    SocketChannel socketChannel = channel.accept();
                    System.out.println("Connection from :" + socketChannel.getRemoteAddress());
                    //将socketChannel的读事件注册在seletor上
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }

                //如果准备好的事件是读事件
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                //基于通道将数据读取到buff
                    channel.read(buffer);
                    String request = new String(buffer.array()).trim();
                    buffer.clear();
                    System.out.println("receive request : " +channel.getRemoteAddress()+"msg : "+ request );
                    String respone = "respone" + request +"\n";
                    //基于通道返回请求内容
                    channel.write(ByteBuffer.wrap(respone.getBytes()));
                }
                //移除已经处理过的事件
                iterator.remove();
            }
        }

    }
}
