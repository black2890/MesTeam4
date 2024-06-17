package com.mesproject.entity;

import com.mesproject.constant.processType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Process {
    @Id
    @GeneratedValue
    private Long processId;

    @Enumerated(EnumType.STRING)
    private processType processType;

    private LocalDateTime processDuration;

}
