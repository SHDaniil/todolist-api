package com.nitinol.todolistapi.repo;

import com.nitinol.todolistapi.persist.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByList(List list, Pageable pageable);
}
