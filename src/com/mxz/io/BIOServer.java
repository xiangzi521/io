package com.mxz.io;

import jdk.nashorn.internal.ir.RuntimeNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description: java类作用描述
 * @Author: struggle
 * @CreateDate: 2019/11/17 13:56
 * @Version: 1.0
 */
public class BIOServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(7777);
        System.out.println("serverSocket :" + serverSocket);
        while (true) {
            // 阻塞等待客户端链接
            Socket accept = serverSocket.accept();

            try {
                System.out.println("connect to :" + accept);
                BufferedReader br = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                while (true) {
                    System.out.println("before");
                    //阻塞 main是不是一值在等待
                    String s = br.readLine();
                    System.out.println("after");
                    //输出接收到的内容
                    System.out.println("response:"+accept.getLocalSocketAddress()+"msg:"+s);
                    String string = "response:"+s+".\n";
                    accept.getOutputStream().write(string.getBytes());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
