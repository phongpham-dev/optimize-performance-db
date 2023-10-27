package research.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import research.repository.EventHolder;

@Controller
@RequestMapping("/monitors")
public class MySqlEventController {

    @Autowired
    EventHolder eventHolder;

    @GetMapping(value = "/internal")
    public String monitor(Model model) {
        model.addAllAttributes(eventHolder.getMonitorHtml());
        return "monitor";
    }
}
