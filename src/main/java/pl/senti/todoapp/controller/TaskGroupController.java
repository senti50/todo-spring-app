package pl.senti.todoapp.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.senti.todoapp.logic.TaskGroupService;
import pl.senti.todoapp.model.ProjectStep;
import pl.senti.todoapp.model.Task;
import pl.senti.todoapp.model.TaskRepository;
import pl.senti.todoapp.model.projection.GroupReadModel;
import pl.senti.todoapp.model.projection.GroupTaskWriteModel;
import pl.senti.todoapp.model.projection.GroupWriteModel;
import pl.senti.todoapp.model.projection.ProjectWriteModel;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@IllegalExceptionProcessing
@RequestMapping("/groups")
public class TaskGroupController {
    private final TaskGroupService taskGroupService;
    private final TaskRepository repository;

    public TaskGroupController(TaskGroupService taskGroupService, TaskRepository repository) {
        this.taskGroupService = taskGroupService;
        this.repository = repository;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String showGroups(Model model){
        model.addAttribute("group", new GroupWriteModel());
        return "groups";
    }

    @ModelAttribute("groups")
    List<GroupReadModel> getGroups() {
        return taskGroupService.readAll();
    }

    @PostMapping(params = "addTask",produces = MediaType.TEXT_HTML_VALUE)
    String addTask(@ModelAttribute("group") GroupWriteModel current) {
        current.getTasks().add(new GroupTaskWriteModel());
        return "groups";
    }

    @PostMapping(produces = MediaType.TEXT_HTML_VALUE,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addGroup(
            @ModelAttribute("group") @Valid GroupWriteModel current,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "groups";
        }
        taskGroupService.crateGroup(current);
        model.addAttribute("group", new GroupWriteModel());
        model.addAttribute("groups", getGroups());
        model.addAttribute("message", "Dodano grupę!");
        return "groups";
    }



    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel toCreate) {
        GroupReadModel result=taskGroupService.crateGroup(toCreate);
        return ResponseEntity.created(URI.create("/"+result.getId())).body(result);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<GroupReadModel>> readAllGroups(){
    return ResponseEntity.ok(taskGroupService.readAll());
    }

    @GetMapping(value= "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable long id){
        return ResponseEntity.ok(repository.findAllByGroup_Id(id));
    }

    @Transactional
    @ResponseBody
    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable long id){
        taskGroupService.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

}
