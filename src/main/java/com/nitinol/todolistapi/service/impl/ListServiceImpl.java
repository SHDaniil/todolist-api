package com.nitinol.todolistapi.service.impl;

import com.nitinol.todolistapi.persist.List;
import com.nitinol.todolistapi.repo.ListRepository;
import com.nitinol.todolistapi.service.ListService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Реализация интерфейса сервиса списков
 */
@Service
public class ListServiceImpl implements ListService {
    @Autowired
    private ListRepository listRepository;

    @Override
    public Page<List> lists(Pageable pageable) {
        return listRepository.findAll(pageable);
    }

    @Override
    public void create(List list) {
        list.setCreationDate(LocalDateTime.now());
        list.setChangeDate(LocalDateTime.now());
        listRepository.save(list);
    }

    @Override
    public boolean update(List list, UUID id) {
        if (!listRepository.existsById(id)){
            return false;
        }

        List listFromDb = listRepository.getOne(id);
        BeanUtils.copyProperties(list, listFromDb, "id", "creationDate");
        listFromDb.setChangeDate(LocalDateTime.now());
        listRepository.save(listFromDb);
        return true;
    }

    @Override
    public boolean delete(UUID id) {
        if (!listRepository.existsById(id)){
            return false;
        }

        listRepository.deleteById(id);
        return true;
    }
}
