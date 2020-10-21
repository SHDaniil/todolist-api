package com.nitinol.todolistapi.controller;

import com.nitinol.todolistapi.persist.*;
import com.nitinol.todolistapi.repo.TaskRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("lists/{id}/tasks")
public class TaskController {

    private static final int DEFAULT_LIMIT = 10;

    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public Page<Task> tasks(@PathVariable("id") List list,
                            @RequestParam Optional<Integer> page,
                            @RequestParam Optional<Integer> limit,
                            @RequestParam Optional<String> orderBy,
                            @RequestParam Optional<String> order){

        if(limit.isPresent() && limit.get() > 100){
            limit = Optional.of(DEFAULT_LIMIT);
        }

        return taskRepository.findByList(list,
                                        PageRequest.of(page.orElse(0),
                                        limit.orElse(DEFAULT_LIMIT),
                                        Sort.Direction.fromString(order.orElse("ASC")),
                                        orderBy.orElse("id")));
    }

    @GetMapping("{taskId}")
    public Task getOneCase(@PathVariable("taskId") Task task){
        return task;
    }

    @PostMapping
    public Task create(@PathVariable("id") List list,
                       @RequestBody Task task){
        task.setCreationDate(LocalDateTime.now());
        task.setChangeDate(LocalDateTime.now());
        task.setComplete(false);
        task.setList(list);
        return taskRepository.save(task);
    }

    @PutMapping("{taskId}")
    public Task update(@PathVariable("taskId") Task taskFromDb,
                       @RequestBody Task task){
        BeanUtils.copyProperties(task, taskFromDb, "id", "creationDate", "list");
        taskFromDb.setChangeDate(LocalDateTime.now());
        return taskRepository.save(taskFromDb);
    }

    @PostMapping("/mark-done/{taskId}")
    public Task markDone(@PathVariable("taskId") Task task){
        task.setComplete(true);
        return taskRepository.save(task);
    }

    @DeleteMapping("{task_id}")
    public void delete(@PathVariable("task_id") Task task){
        taskRepository.delete(task);
    }

}
