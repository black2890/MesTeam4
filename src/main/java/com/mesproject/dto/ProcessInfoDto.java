package com.mesproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Data
@Getter @Setter
public class ProcessInfoDto {

    private String processText;
    private LocalDateTime searchDate;

}
