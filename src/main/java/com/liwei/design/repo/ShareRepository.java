package com.liwei.design.repo;

import com.liwei.design.model.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ShareRepository extends JpaRepository<Share, String> {
    @Query("select s from Share s where s.user = ?1 and s.spath is not null")
    List<Share> getListByUser(String user);

    @Query("select s.path from Share s where s.user = ?1 and s.spath is null")
    List<String> getListByUserAndSpath(String user);

    @Query("select s.path from Share s where s.spath = ?1 and s.secret = ?2")
    String getBysPathAndSecret(String sPath, String secret);

    @Query("select s from Share s where s.spath = ?1")
    List<Share> getBysPath(String sPath);

    @Modifying
    @Transactional
    @Query("delete from Share s where s.path = ?1")
    void deleteByUrl(String url);
}
