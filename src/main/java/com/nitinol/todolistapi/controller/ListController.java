package com.nitinol.todolistapi.controller;

import com.nitinol.todolistapi.exceptions.*;
import com.nitinol.todolistapi.persist.List;
import com.nitinol.todolistapi.service.impl.ListServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Контроллер списков
 */
@RestController
@RequestMapping("lists")
public class ListController {

    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LENGTH_TITLE = 250;

    private final ListServiceImpl listService;

    /**
     * Конструктор контроллера листов
     *
     * @param listService создание объекта сервиса листов
     */
    @Autowired
    public ListController(ListServiceImpl listService) {
        this.listService = listService;
    }

    /**
     * Поиск всех списков
     *
     * @param page номер страницы
     * @param limit количество элементов на странице
     * @param orderBy по какому элементу сортировать
     * @param order метод сортировки ASC or DESC
     * @return возвращет страницу со списками
     */
    @ApiOperation(value = "Вывод всех списков")
    @GetMapping
    public ResponseEntity<Page<List>> lists(@RequestParam Optional<Integer> page,
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
        Page<List> listPage = listService.lists(pageable);

        return listPage != null && !listPage.isEmpty()
                ? new ResponseEntity<>(listPage, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Поиск списка по ID
     *
     * @param list Список по ID
     * @return найденный список или код ошибки
     */
    @ApiOperation(value = "Поиск списка по ID")
    @GetMapping("{id}")
    public ResponseEntity<List> getOneList(@PathVariable("id") List list){

        return list != null
                ? new ResponseEntity<>(list, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Добавление нового списка
     *
     * @param list список который нужно добавить
     * @return HTTP код
     */
    @ApiOperation(value = "Добавление нового списка")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody List list) throws BigLengthException, ConflictException {
        if(list.getTitle().length() > MAX_LENGTH_TITLE) {
            throw new BigLengthException();
        }

        if(listService.readByTitle(list.getTitle()) != null) {
            throw new ConflictException();
        }

        listService.create(list);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Изменение списка
     *
     * @param id ID списка
     * @param list Измененный список
     * @return HTTP код
     */
    @ApiOperation(value = "Изменение списка")
    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") UUID id,
                                    @RequestBody List list
    ) throws BigLengthException {
        if(list.getTitle().length() > MAX_LENGTH_TITLE) {
            throw new BigLengthException();
        }

        final boolean updated = listService.update(list, id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * Удаление списка
     *
     * @param id ID списка
     * @return HTTP код
     */
    @ApiOperation(value = "Удаление списка")
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") UUID id){
        final boolean deleted = listService.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
