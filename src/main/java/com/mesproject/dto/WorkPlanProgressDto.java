package com.mesproject.dto;

import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class WorkPlanProgressDto {
    private String productName;
    private List<ProcessProgressDto> processProgressList;


}
