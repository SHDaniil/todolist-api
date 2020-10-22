package com.nitinol.todolistapi.controller;

import com.nitinol.todolistapi.persist.*;
import com.nitinol.todolistapi.persist.List;
import com.nitinol.todolistapi.service.TaskServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 *
 */
@RestController
@RequestMapping("lists/{id}/tasks")
public class TaskController {

    private static final int DEFAULT_LIMIT = 10;

    private final TaskServiceImpl taskService;

    /**
     * @param taskService содание объекта сервиса тасков
     */
    @Autowired
    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    /**
     * @param list
     * @param page
     * @param limit
     * @param orderBy
     * @param order
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<Task>> tasks(@PathVariable("id") List list,
                            @RequestParam Optional<Integer> page,
                            @RequestParam Optional<Integer> limit,
                            @RequestParam Optional<String> orderBy,
                            @RequestParam Optional<String> order){

        if(limit.isPresent() && limit.get() > 100){
            limit = Optional.of(DEFAULT_LIMIT);
        }

        Pageable pageable = PageRequest.of(page.orElse(0),
                            limit.orElse(DEFAULT_LIMIT),
                            Sort.Direction.fromString(order.orElse("ASC")),
                            orderBy.orElse("id"));
        Page<Task> taskPage = taskService.tasks(list, pageable);

        return taskPage != null && !taskPage.isEmpty()
                ? new ResponseEntity<>(taskPage, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("{taskId}")
    public ResponseEntity<Task> getOneTask(@PathVariable(name = "taskId") UUID taskId){
        Task task = taskService.getOneTask(taskId);

        return task != null
                ? new ResponseEntity<>(task, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable("id") List list,
                                    @RequestBody Task task){
        taskService.create(list, task);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("{taskId}")
    public ResponseEntity<?> update(@PathVariable(name = "id") UUID id,
                                    @PathVariable(name = "taskId") UUID taskId,
                                    @RequestBody Task task){
        final boolean updated = taskService.update(task, id, taskId);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") UUID id,
                       @PathVariable(name = "taskId") UUID taskId){
        final boolean deleted = taskService.delete(id, taskId);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/mark-done/{taskId}")
    public ResponseEntity<?> markDone(@PathVariable(name = "id") UUID id,
                                      @PathVariable(name = "taskId") UUID taskId){
        final boolean markDone = taskService.markDone(id, taskId);

        return markDone
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
