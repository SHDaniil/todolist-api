package com.nitinol.todolistapi.service;

import com.nitinol.todolistapi.persist.*;
import org.springframework.data.domain.*;

import java.util.UUID;

public interface TaskService {

    Page<Task> tasks(List list, Pageable pageable);

    Task getOneTask(UUID id);

    Task create(List list, Task task);

    boolean update(Task task, UUID id, UUID taskId);

    boolean delete(UUID id, UUID taskId);

    boolean markDone(UUID id, UUID taskId);

}
