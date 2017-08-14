package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GreetingController {

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    public GreetingController(SimpMessageSendingOperations simpMessageSendingOperations) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    @Autowired
    private SimpMessagingTemplate template;

    /**
     * 用户连接上websocket，服务器返回1条信息，js中需使用/app/init  路径
     * @return
     * @throws Exception
     */
    @SubscribeMapping("/init")
    public Greeting initData() throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, "  + "!");
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }

    /**
     * 这里用的是@SendToUser，这就是发送给单一客户端的标志。本例中，
     * 客户端接收一对一消息的主题应该是“/user/” + 用户Id + “/message” ,这里的用户id可以是一个普通的字符串，只要每个用户端都使用自己的id并且服务端知道每个用户的id就行。
     * @return
     */
    @MessageMapping("/message/{userId}")
    @SendToUser(value = "/message", broadcast = false)
    public Greeting handleSubscribe(@DestinationVariable String userId) {
        System.out.println("this is the @SubscribeMapping('/marco')");
        return new Greeting("I am a msg from SubscribeMapping('/macro').");
    }

    /**
     * 测试对指定用户发送消息方法
     * @return
     */
    @MessageMapping("/send/{userId}")
    public Greeting send(@DestinationVariable String userId) {
        simpMessageSendingOperations.convertAndSendToUser(userId, "/message", new Greeting("I am a msg from SubscribeMapping('/macro')."));
        return new Greeting("I am a msg from SubscribeMapping('/macro').");
    }



}
