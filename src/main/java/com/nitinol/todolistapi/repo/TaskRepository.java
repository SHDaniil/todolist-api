package com.nitinol.todolistapi.repo;

import com.nitinol.todolistapi.persist.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий таблицы тасков
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    /**
     * Поиск тасков по листу
     *
     * @param list Список по которому осушествляется поиск
     * @param pageable страница
     * @return возвращет страницу с тасками
     */
    Page<Task> findByList(List list, Pageable pageable);
}
