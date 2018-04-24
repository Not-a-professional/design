package com.liwei.design.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "delete")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = "Delete.findAll", query = "select d from Delete d")
public class Delete implements Serializable {
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
