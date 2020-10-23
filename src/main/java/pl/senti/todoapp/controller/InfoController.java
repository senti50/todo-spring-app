package pl.senti.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.senti.todoapp.TaskConfigurationProperties;

@RestController
@RequestMapping("/info")
public class InfoController {


    private DataSourceProperties dataSourceProperties;
    private TaskConfigurationProperties myProp;

    public InfoController(DataSourceProperties dataSourceProperties, TaskConfigurationProperties myProp) {
        this.dataSourceProperties = dataSourceProperties;
        this.myProp = myProp;
    }

    @GetMapping("/url")
    String url() {
        return dataSourceProperties.getUrl();
    }

    @GetMapping("/prop")
    boolean myProp() {
        return myProp.getTemplate().isAllowMultipleTasks();
    }
}
