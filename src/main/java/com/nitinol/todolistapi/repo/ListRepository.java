package com.nitinol.todolistapi.repo;

import com.nitinol.todolistapi.persist.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListRepository extends JpaRepository<List, Long> {

}
