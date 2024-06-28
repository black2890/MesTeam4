package com.mesproject.controller;

import com.fasterxml.jackson.databind.deser.BasicDeserializerFactory;
import com.mesproject.dto.ProcessInfoDto;
import com.mesproject.dto.WorkPlanProgressDto;
import com.mesproject.service.WorkPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Controller
public class MainController {

    @PostMapping("/api/createMainChart")
    public ResponseEntity<?> createChart(@RequestBody LocalDateTime request) {
        return ResponseEntity.ok().build();
    }

}
