package com.liwei.design.repo;

import com.liwei.design.model.ticketVolume;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ticketVolumeRepository extends JpaRepository<ticketVolume, Long> {
  @Query("select t from ticketVolume t where t.user <> ?1 and t.user like ?2 and t.status = '0'")
  List<ticketVolume> findAllByUserAndStatus(String user, String username, Pageable pageable);

  @Query("select t from ticketVolume t where t.user = ?1 and t.status = '0'")
  List<ticketVolume> findAllByUserAndStatus(String username);
}
