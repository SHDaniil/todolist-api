package com.nitinol.todolistapi.service;

import com.nitinol.todolistapi.persist.*;
import org.springframework.data.domain.*;

import java.util.UUID;

/**
 * Интерфей сервиса тасков
 */
public interface TaskService {

    /**
     * Вывод всех тасков по списку
     *
     * @param list Список по которому осушествляется поиск
     * @param pageable страница
     * @return возвращет страницу с тасками
     */
    Page<Task> tasks(List list, Pageable pageable);

    /**
     * Добавление нового списка
     *
     * @param list список
     * @param task таск который нужно добавить
     */
    void create(List list, Task task);

    /**
     * Изменение таска
     *
     * @param task Измененный таск
     * @param id ID списка
     * @param taskId ID таска
     * @return true/false
     */
    boolean update(Task task, UUID id, UUID taskId);

    /**
     * Удаление таска
     *
     * @param id ID списка
     * @param taskId ID таска
     * @return true/false
     */
    boolean delete(UUID id, UUID taskId);

    /**
     * Отметить таск как законченный
     *
     * @param id ID списка
     * @param taskId ID таска
     * @return true/false
     */
    boolean markDone(UUID id, UUID taskId);

    /**
     * @param title заголовок
     * @return Найденный таск по заголовку
     */
    Task readByTitle(String title);

}
