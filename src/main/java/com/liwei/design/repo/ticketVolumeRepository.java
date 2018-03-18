package com.liwei.design.repo;

import com.liwei.design.model.ticketVolume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ticketVolumeRepository extends JpaRepository<ticketVolume, Long> {
}
