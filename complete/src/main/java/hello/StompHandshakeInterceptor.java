package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by xiaoxiao on 2017/8/2.
 */
//@Component
public class StompHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    protected Logger logger = LoggerFactory.getLogger(getClass());

//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//
//        logger.info("beforeHandshake");
//        if (request instanceof ServletServerHttpRequest) {
//
//            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
//            HttpSession session = servletRequest.getServletRequest().getSession(false);
//
//            String userId = httpServletRequest.getParameter("userId");
//            String userId2 = httpServletRequest.getHeader("userId");
//            logger.info(userId);
//            Object attrUserId = session.getAttribute("userId");
//            if (attrUserId != null) {
//                String userId = attrUserId.toString();
//
//                logger.info("beforeHandshake : " + userId);
//            }
//        }
//
//        return true;
//    }




    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        logger.info("beforeHandshake");

        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
        String userId = httpServletRequest.getParameter("userId");
        logger.info("userId : " + userId);
        return false;
//        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        logger.info("afterHandshake");
        super.afterHandshake(request, response, wsHandler, exception);
    }
}
