package com.mesproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Routing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routingId;
    // private Long processId;

    private String routingName;
}
