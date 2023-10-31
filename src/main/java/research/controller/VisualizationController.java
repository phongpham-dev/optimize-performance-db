package research.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import research.service.SQLStatementStatistics;

@Controller
@RequestMapping("/visualize")
@AllArgsConstructor
public class VisualizationController {

    private final SQLStatementStatistics sqlStatementStatistics;

    @GetMapping("/internal")
    public String monitor(Model model) {
        System.out.println("controller " + sqlStatementStatistics.getMonitorHtml());
        model.addAllAttributes(sqlStatementStatistics.getMonitorHtml());
        return "visualize";
    }

    @GetMapping("/internal1")
    public String monitor1(Model model) {
        System.out.println("controller " + sqlStatementStatistics.getMonitorHtml());
        model.addAllAttributes(sqlStatementStatistics.getMonitorHtml01());
        return "visualize1";
    }

    @GetMapping("/all")
    public String monitorAll(Model model) {
        System.out.println("controller " + sqlStatementStatistics.getMonitorHtml());
        model.addAllAttributes(sqlStatementStatistics.getMonitorHtml());
        return "visualize";
    }
}
