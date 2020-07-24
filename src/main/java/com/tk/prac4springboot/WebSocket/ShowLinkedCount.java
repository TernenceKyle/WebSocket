package com.tk.prac4springboot.WebSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint("/BitCoin")
@Component
public class ShowLinkedCount {
    private static AtomicInteger count = new AtomicInteger();
//    private static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();
    private static Collection<Session> sessionList = Collections.synchronizedCollection(new ArrayList<Session>());
    private static Session session;
    private static Logger logger = LoggerFactory.getLogger(ShowLinkedCount.class);
    public void sendMessage(Session session,String message) throws IOException {
        if (session!=null)
        {
            synchronized (session)
            {
                session.getBasicRemote().sendText(message);
            }
        }
    }
//    public void sendMessageTo(String username,String message) {
//        Session session = sessionMap.get(username);
//        try {
//            sendMessage(session, message);
//        } catch (Exception e)
//        {
//            logger.error("发送信息出错："+e.getCause());
//        }
//    }
    @OnOpen
    public void onOpen(Session session)
    {
        String uid = "cli"+count.toString();
        this.session = session;
        sessionList.add(session);
        addCount();
        System.out.println(uid+"已连接 ! 当前访问人数: "+count);
    }
    @OnClose
    public void onClose(){
        sessionList.remove(this.session);
        subCount();
        System.out.println(session.toString()+" has been Disconnected  当前在线人数: "+count);
    }
    @OnMessage
    public void onMessage(String message){
        String sinfo = "服务端收到信息.";
            try {
                sendMessage(session, sinfo);
            }catch (Exception e)
            {
                System.err.println("反馈客户端信息错误!");
                logger.error("反馈信息错误!");
            }

    }
    public static void broadCast(String message)
    {
        for (Session session:sessionList)
        {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                System.err.println(e.getCause());
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session,Throwable throwable){
        System.err.println("发生错误!");
        throwable.printStackTrace();
    }
    public static void  addCount(){
        count.incrementAndGet();
    }
    public static void subCount(){
        count.decrementAndGet();
    }
    public static String getTotalCount(){
        return count.toString();
    }
}
