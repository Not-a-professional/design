package com.liwei.design.repo;

import com.liwei.design.model.Trash;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DeleteRepository extends JpaRepository<Trash, String> {
    @Query("select d from Trash d where d.path = ?1")
    List<Trash> findAllByPath(String path);

    @Query("select d from Trash d where datediff(NOW() ,d.date) = 30")
    List<Trash> findAllByDateBefore30Days();

    @Transactional
    @Modifying
    @Query("delete from Trash d where d.path = ?1")
    void deleteByPath(String path);

    @Query("select d from Trash d where d.user = ?1")
    List<Trash> findAllByUser(String username, Pageable pageable);
}
