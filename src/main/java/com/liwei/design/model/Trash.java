package com.liwei.design.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "trash")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = "Trash.findAll", query = "select d from Trash d")
public class Trash implements Serializable {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Getter
    @Setter
    private String path;

    @Getter
    @Setter
    private Timestamp date;

    @Getter
    @Setter
    private String user;
}
