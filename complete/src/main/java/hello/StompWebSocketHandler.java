package hello;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xiaoxiao on 2017/8/2.
 */
@Component
public class StompWebSocketHandler extends TextWebSocketHandler {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    //在线人数
    private static int onlineCount = 0;


    //用于保存HttpSession与WebSocketSession的映射关系
    public static final Map<String, WebSocketSession> userSocketSessionMap;

    static {
        userSocketSessionMap = new ConcurrentHashMap<String, WebSocketSession>();
    }
//    private final static List<WebSocketSession> sessions = Collections.synchronizedList(new ArrayList<WebSocketSession>());
    //接收文本消息，并发送出去
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        chatTextMessageHandler(message.getPayload());



    }
    //连接建立后处理
    @SuppressWarnings("unchecked")
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.debug("connect to the websocket chat success......");



    }
    //抛出异常时处理
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){
            session.close();
        }
        logger.debug("websocket chat connection closed......");
        //无需广播；或司机端掉线，会员端是否需推送消息？
        if (session.getAttributes().get("userId") != null) {
            String userId = session.getAttributes().get("userId").toString();
            userSocketSessionMap.remove(userId);
        }

    }
    //连接关闭后处理
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

        logger.debug("websocket chat connection closed......");
        if (session.getAttributes().get("userId") != null) {
            String userId = session.getAttributes().get("userId").toString();
            userSocketSessionMap.remove(userId);
            onlineCountStatus("3");       //在线数减1
        }


    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 广播；给所有在线用户发送消息
     * @param message
     * @throws Exception
     */
    public void broadcast(final TextMessage message, String userId) throws IOException {

        Iterator<Map.Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();

        //可评估使用消息队列
        while (it.hasNext()) {

            final Map.Entry<String, WebSocketSession> entry = it.next();

            if (!userId.equals(entry.getKey()) && entry.getValue().isOpen()) {
                // entry.getValue().sendMessage(message);
                new Thread(new Runnable() {

                    public void run() {
                        try {
                            if (entry.getValue().isOpen()) {
                                entry.getValue().sendMessage(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }).start();
            }

        }

    }


    /**
     * 给某个用户发送消息
     *
     * @param userId
     * @param message
     * @throws IOException
     */
    public void sendMessageToUser(String userId, TextMessage message) throws IOException {
        WebSocketSession session = userSocketSessionMap.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(message);
        }
    }


    public static synchronized int onlineCountStatus(String operaType) {

        if ("1".equals(operaType)) {
            return StompWebSocketHandler.onlineCount;
        }else if ("2".equals(operaType)) {
            return StompWebSocketHandler.onlineCount++;
        }else if ("3".equals(operaType)){
            return StompWebSocketHandler.onlineCount--;
        }


        return StompWebSocketHandler.onlineCount;
    }


}
