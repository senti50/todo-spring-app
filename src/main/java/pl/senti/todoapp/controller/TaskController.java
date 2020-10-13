package pl.senti.todoapp.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.senti.todoapp.model.Task;
import pl.senti.todoapp.model.TaskRepository;


import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
class TaskController {
    private  static  final Logger logger= LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;


    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/tasks")
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate) {
        Task result=repository.save(toCreate);
        return ResponseEntity.created(URI.create("/"+result.getId())).body(result);
    }

    @GetMapping(value = "/tasks",params = {"!sort","!page","!size"})
    ResponseEntity<List<Task>>readAllTask(){
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }
    @GetMapping("/tasks")
    ResponseEntity<List<Task>>readAllTask(Pageable page){
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<Task>readTask(@PathVariable long id){
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }@Transactional
    @PutMapping("/tasks/{id}")
    ResponseEntity<?> updateTask(@PathVariable long id, @RequestBody @Valid Task toUpdate){
        if (!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id).ifPresent(task -> task.updateFrom(task));
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/tasks/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable long id){
        if (!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id).ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }
}
