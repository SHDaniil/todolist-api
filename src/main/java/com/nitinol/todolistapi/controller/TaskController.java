package com.nitinol.todolistapi.controller;

import com.nitinol.todolistapi.exceptions.*;
import com.nitinol.todolistapi.persist.List;
import com.nitinol.todolistapi.persist.*;
import com.nitinol.todolistapi.service.impl.TaskServiceImpl;
import io.swagger.annotations.ApiOperation;
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
    private static final int MAX_LENGTH_TITLE = 250;
    private static final int MAX_LENGTH_DESCRIPTION = 250;

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
    @ApiOperation(value = "Вывод всех тасков по списку")
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
     * Поиск списка по ID
     *
     * @param task Таск по ID
     * @return найденный таск или код ошибки
     */
    @ApiOperation(value = "Поиск списка по ID")
    @GetMapping("{taskId}")
    public ResponseEntity<Task> getOneTask(@PathVariable("taskId") Task task){

        return task != null
                ? new ResponseEntity<>(task, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Добавление нового таска
     *
     * @param list список
     * @param task таск который нужно добавить
     * @return HTTP код
     */
    @ApiOperation(value = "Добавление нового списка")
    @PostMapping
    public ResponseEntity<?> create(@PathVariable("id") List list,
                                    @RequestBody Task task) throws BigLengthException, ConflictException{
        if(task.getTitle().length() > MAX_LENGTH_TITLE || task.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
            throw new BigLengthException();
        }

        if(taskService.readByTitle(task.getTitle()) != null) {
            throw new ConflictException();
        }

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
    @ApiOperation(value = "Изменение таска")
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
    @ApiOperation(value = "Удаление таска")
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
    @ApiOperation(value = "Отметить таск как законченный")
    @PostMapping("/mark-done/{taskId}")
    public ResponseEntity<?> markDone(@PathVariable(name = "id") UUID id,
                                      @PathVariable(name = "taskId") UUID taskId){
        final boolean markDone = taskService.markDone(id, taskId);

        return markDone
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
