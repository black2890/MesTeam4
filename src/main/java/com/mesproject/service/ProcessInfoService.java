package com.mesproject.service;

import com.mesproject.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcessInfoService {

    private final ProcessRepository processRepository;

    public List<String> getProcessList(){
        return processRepository.findDistinctProcessTypesOrdered();
    }
}
