package com.nitinol.todolistapi.controller;

import com.nitinol.todolistapi.persist.List;
import com.nitinol.todolistapi.service.ListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("lists")
public class ListController {

    private static final int DEFAULT_LIMIT = 10;

    private final ListServiceImpl listService;

    @Autowired
    public ListController(ListServiceImpl listService) {
        this.listService = listService;
    }

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

    @GetMapping("{id}")
    public ResponseEntity<List> getOneList(@PathVariable(name = "id") UUID id){
        List list = listService.getOneList(id);

        return list != null
                ? new ResponseEntity<>(list, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody List list){
        listService.create(list);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") UUID id,
                                    @RequestBody List list
    ){
        final boolean updated = listService.update(list, id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") UUID id){
        final boolean deleted = listService.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
