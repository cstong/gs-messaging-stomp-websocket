package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by xiaoxiao on 2017/8/10.
 */
public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        logger.info("PresenceChannelInterceptor : preSend");


        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        // StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(accessor != null) {
            StompCommand command = accessor.getCommand();
            if(command != null) {
                if(command == StompCommand.CONNECT) {

                    Object raw = accessor.getHeader(SimpMessageHeaderAccessor.NATIVE_HEADERS);
                    if (raw instanceof Map) {
                        Object userId = ((Map) raw).get("userId");
                        Object token = ((Map) raw).get("userId");
                        if (userId instanceof LinkedList) {
                            accessor.setUser(new UserVo(((LinkedList) userId).get(0).toString()));
                        }
                        if(!validateSubscription(userId, token))
                        {
                            throw new MessagingException("No permission for this topic");
                        }

                    }

                    // I don't need any more to create a new message
                    //return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
                }
            }
        }
        return message;
    }

    /**
     * 验证token
     * @param userId
     * @param token
     * @return
     */
    private boolean validateSubscription(Object userId, Object token) {

        return true;
    }


    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {

        logger.info("PresenceChannelInterceptor : postSend");
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);

        // ignore non-STOMP messages like heartbeat messages
        if(sha.getCommand() == null) {
            return;
        }

        String sessionId = sha.getSessionId();

        switch(sha.getCommand()) {
            case CONNECT:
                logger.debug("STOMP Connect [sessionId: " + sessionId + "]");
                break;
            case CONNECTED:
                logger.debug("STOMP Connected [sessionId: " + sessionId + "]");
                break;
            case DISCONNECT:
                logger.debug("STOMP Disconnect [sessionId: " + sessionId + "]");
                break;
            default:
                break;

        }
    }
}