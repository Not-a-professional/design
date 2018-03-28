package com.liwei.design.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ticket_volume")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = "ticketVolume.findAll", query = "select t from ticketVolume t")
public class ticketVolume implements Serializable {
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
    private String reason;

    @Setter
    @Getter
    private String status;
}
