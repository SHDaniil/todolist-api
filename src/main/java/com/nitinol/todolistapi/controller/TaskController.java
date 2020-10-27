package com.nitinol.todolistapi.controller;

import com.nitinol.todolistapi.persist.List;
import com.nitinol.todolistapi.persist.*;
import com.nitinol.todolistapi.service.impl.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 *  Контроллер тасков
 */
@RestController
@RequestMapping("lists/{id}/tasks")
public class TaskController {

    private static final int DEFAULT_LIMIT = 10;

    private final TaskServiceImpl taskService;

    /**
     * Конструктор контроллера тасков
     *
     * @param taskService содание объекта сервиса тасков
     */
    @Autowired
    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    /**
     * Вывод всех тасков по списку
     *
     * @param list Список по которому осушествляется поиск
     * @param page номер страницы
     * @param limit количество элементов на странице
     * @param orderBy по какому элементу сортировать
     * @param order метод сортировки ASC or DESC
     * @return возвращет страницу с тасками
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

    /**
     * Поиск таска по ID
     *
     * @return найденный таск или код ошибки
     */
    @GetMapping("{taskId}")
    public ResponseEntity<Task> getOneTask(@PathVariable("taskId") Task task){

        return task != null
                ? new ResponseEntity<>(task, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Добавление нового списка
     *
     * @param list список
     * @param task таск который нужно добавить
     * @return HTTP код
     */
    @PostMapping
    public ResponseEntity<?> create(@PathVariable("id") List list,
                                    @RequestBody Task task){
        taskService.create(list, task);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Изменение таска
     *
     * @param id ID списка
     * @param taskId ID таска
     * @param task Измененный таск
     * @return HTTP код
     */
    @PutMapping("{taskId}")
    public ResponseEntity<?> update(@PathVariable(name = "id") UUID id,
                                    @PathVariable(name = "taskId") UUID taskId,
                                    @RequestBody Task task){
        final boolean updated = taskService.update(task, id, taskId);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * Удаление таска
     *
     * @param id ID списка
     * @param taskId ID таска
     * @return HTTP код
     */
    @DeleteMapping("{taskId}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") UUID id,
                       @PathVariable(name = "taskId") UUID taskId){
        final boolean deleted = taskService.delete(id, taskId);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * Отметить таск как законченный
     *
     * @param id ID списка
     * @param taskId ID таска
     * @return HTTP код
     */
    @PostMapping("/mark-done/{taskId}")
    public ResponseEntity<?> markDone(@PathVariable(name = "id") UUID id,
                                      @PathVariable(name = "taskId") UUID taskId){
        final boolean markDone = taskService.markDone(id, taskId);

        return markDone
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
