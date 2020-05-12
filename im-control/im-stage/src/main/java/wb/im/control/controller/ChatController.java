package wb.im.control.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class ChatController {

    @RequestMapping(value = "testChat")
    public String testChat() {
        return "chat";
    }
}
