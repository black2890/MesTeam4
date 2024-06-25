package com.mesproject.entity;

import com.mesproject.constant.ProcessType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Process {
    @Id
    @GeneratedValue
    private Long processId;

    @Enumerated(EnumType.STRING)
    private ProcessType processType;

    private LocalDateTime processDuration;

}
