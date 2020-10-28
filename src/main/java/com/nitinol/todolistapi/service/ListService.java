package com.nitinol.todolistapi.service;

import com.nitinol.todolistapi.persist.List;
import org.springframework.data.domain.*;

import java.util.UUID;

/**
 * Интерфей сервиса списков
 */
public interface ListService {

    /**
     * Поиск всех списков
     *
     * @param pageable страница
     * @return возвращет страницу со списками
     */
    Page<List> lists(Pageable pageable);

    /**
     * Добавление нового списка
     *
     * @param list список который нужно добавить
     */
    void create(List list);

    /**
     * Изменение списка
     *
     * @param list Измененный список
     * @param id ID списка
     * @return true/false
     */
    boolean update(List list, UUID id);

    /**
     * Удаление списка
     *
     * @param id ID списка
     * @return true/false
     */
    boolean delete(UUID id);

    /**
     * @param title заголовок
     * @return Найденный список по заголовку
     */
    List readByTitle(String title);

}
