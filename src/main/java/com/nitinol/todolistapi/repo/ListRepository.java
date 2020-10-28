package com.nitinol.todolistapi.repo;

import com.nitinol.todolistapi.persist.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий таблицы списков
 */
@Repository
public interface ListRepository extends JpaRepository<List, UUID> {

    /**
     * @param title заголовок
     * @return Найденный список по заголовку
     */
    List readByTitle(String title);
}
