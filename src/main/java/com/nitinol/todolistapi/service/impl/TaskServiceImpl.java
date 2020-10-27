package com.nitinol.todolistapi.service.impl;

import com.nitinol.todolistapi.persist.*;
import com.nitinol.todolistapi.repo.*;
import com.nitinol.todolistapi.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Реализация интерфейса сервиса тасков
 */
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ListRepository listRepository;

    @Override
    public Page<Task> tasks(List list, Pageable pageable) {
        return taskRepository.findByList(list, pageable);
    }

    @Override
    public void create(List list, Task task) {
        task.setCreationDate(LocalDateTime.now());
        task.setChangeDate(LocalDateTime.now());
        task.setComplete(false);
        task.setList(list);
        taskRepository.save(task);
    }

    @Override
    public boolean update(Task task, UUID id, UUID taskId) {
        if (!listRepository.existsById(id)){
            return false;
        }
        if (!taskRepository.existsById(taskId)){
            return false;
        }

        Task taskFromDb = taskRepository.getOne(id);
        BeanUtils.copyProperties(task, taskFromDb, "id", "creationDate", "list");
        taskFromDb.setChangeDate(LocalDateTime.now());
        taskRepository.save(taskFromDb);
        return true;
    }

    @Override
    public boolean delete(UUID id, UUID taskId) {
        if (!listRepository.existsById(id)){
            return false;
        }
        if (!taskRepository.existsById(taskId)){
            return false;
        }

        taskRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean markDone(UUID id, UUID taskId) {
        if (!listRepository.existsById(id)){
            return false;
        }
        if (!taskRepository.existsById(taskId)){
            return false;
        }

        Task task = taskRepository.getOne(taskId);
        task.setComplete(true);
        taskRepository.save(task);
        return true;
    }
}
