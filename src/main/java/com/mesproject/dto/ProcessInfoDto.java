package com.mesproject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter @Setter
public class ProcessInfoDto {

    private String processText;
    private LocalDateTime searchDate;

}
