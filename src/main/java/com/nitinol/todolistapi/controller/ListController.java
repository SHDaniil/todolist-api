package com.nitinol.todolistapi.controller;

import com.nitinol.todolistapi.persist.List;
import com.nitinol.todolistapi.repo.ListRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("lists")
public class ListController {

    private static final int DEFAULT_LIMIT = 10;

    private final ListRepository listRepository;

    @Autowired
    public ListController(ListRepository listRepository) {
        this.listRepository = listRepository;
    }

    @GetMapping
    public Page<List> lists(@RequestParam Optional<Integer> page,
                            @RequestParam Optional<Integer> limit,
                            @RequestParam Optional<String> orderBy,
                            @RequestParam Optional<String> order){

        if(limit.isPresent() && limit.get() > 100){
            limit = Optional.of(DEFAULT_LIMIT);
        }

        return listRepository.findAll(PageRequest.of(page.orElse(0),
                                    limit.orElse(DEFAULT_LIMIT),
                                    Sort.Direction.fromString(order.orElse("ASC")),
                                    orderBy.orElse("id")));
    }

    @GetMapping("{id}")
    public List getOneList(@PathVariable("id") List list){
        return list;
    }

    @PostMapping
    public List create(@RequestBody List list){
        list.setCreationDate(LocalDateTime.now());
        list.setChangeDate(LocalDateTime.now());
        return listRepository.save(list);
    }

    @PutMapping("{id}")
    public List update(@PathVariable("id") List listFromDb,
                       @RequestBody List list
    ){
        BeanUtils.copyProperties(list, listFromDb, "id", "creationDate");
        listFromDb.setChangeDate(LocalDateTime.now());
        return listRepository.save(listFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") List list){
        listRepository.delete(list);
    }
}
