package com.worldkey.config.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by sang on 16-12-22.
 */
@Controller
public class WsController {
    @MessageMapping("/systemNotice")
    @SendTo("/topic/getResponse")
    public ResponseMessage say(RequestMessage message) {
        System.out.println(message.getMessage());
        return new ResponseMessage(message.getMessage());
    }
    @RequestMapping("ws")
    public String ws(){
        return "webSocket/ws";
    }


}
