package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by xiaoxiao on 2017/8/10.
 */
@Component
public class StompConnectEvent implements ApplicationListener<SessionConnectEvent> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public void onApplicationEvent(SessionConnectEvent event) {
//        SimpMessageHeaderAccessor

        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        String  userId = sha.getNativeHeader("userId").get(0);
        String  token = sha.getNativeHeader("token").get(0);
//        UserVo userVo = new UserVo();
//        userVo.setUserId(userId);
//        userVo.setToken(token);
//        sha.setUser(userVo);

        logger.info("Connect event [sessionId: " + sha.getSessionId() +"; userId: "+ userId + "; token: " + token + " ]");
    }
}
