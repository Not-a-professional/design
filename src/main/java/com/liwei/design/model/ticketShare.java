package com.liwei.design.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ticket_share")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = "ticketShare.findAll", query = "select t from ticketShare t")
public class ticketShare implements Serializable {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Getter
    @Setter
    private String user;

    @Setter
    @Getter
    private String path;

    @Setter
    @Getter
    private String spath;

    @Setter
    @Getter
    private String secret;

    @Setter
    @Getter
    private String status;
}
