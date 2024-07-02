package com.mesproject.dto;

import com.mesproject.constant.ProcessType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class ProcessProgressDto {

    private ProcessType processType;
    private double progress;
}
