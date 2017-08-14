package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Created by xiaoxiao on 2017/8/10.
 */
@Component
public class StompDisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {

        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        logger.info("Disconnect event [sessionId: " + sha.getSessionId() +"]");
    }
}
