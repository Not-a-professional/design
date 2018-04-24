package com.liwei.design.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "share")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = "Share.findAll", query = "select s from Share s")
public class Share implements Serializable{
    @Id
    @Getter
    @Setter
    private String path;

    @Getter
    @Setter
    private String date;

    @Getter
    @Setter
    private String user;


    /**
     * 私密分享的路径,不是null即私密分享，是null即公开分享
     */
    @Getter
    @Setter
    private String spath;

    @Getter
    @Setter
    private String secret;

    @Getter
    @Setter
    private long download;

    @Getter
    @Setter
    private Timestamp expiredTime;

}

