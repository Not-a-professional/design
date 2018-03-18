package com.liwei.design.repo;

import com.liwei.design.model.ticketShare;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ticketShareRepository extends JpaRepository<ticketShare, Long> {
    @Query("select t from ticketShare t where t.user <> ?1 and t.user like ?2 and t.status = '0'")
    List<ticketShare> findAllByUser(String user, String username, Pageable pageable);

    @Query("select t from ticketShare t where t.path = ?1")
    ticketShare findOneByPath(String url);

    @Modifying
    @Transactional
    @Query("delete from ticketShare t where t.id = ?1")
    void deleteById(long id);

    @Modifying
    @Transactional
    @Query("update ticketShare t set t.status = '1' where t.id = ?1")
    void updateById(long id);
}
