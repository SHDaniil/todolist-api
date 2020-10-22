package com.nitinol.todolistapi.service;

import com.nitinol.todolistapi.persist.List;
import org.springframework.data.domain.*;

import java.util.UUID;

public interface ListService {

    Page<List> lists(Pageable pageable);

    List getOneList(UUID id);

    List create(List list);

    boolean update(List list, UUID id);

    boolean delete(UUID id);

}
