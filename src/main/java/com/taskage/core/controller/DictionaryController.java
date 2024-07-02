package com.taskage.core.controller;

import com.taskage.core.service.DictionaryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/core/dictionary")
public class DictionaryController {
    private final DictionaryService dictionaryService;

    @GetMapping("/getStatuses")
    public ResponseEntity<Map<Integer, String>> getStatuses() {
        return ResponseEntity.ok(dictionaryService.getStatuses());
    }

    @GetMapping("/getPriorities")
    public ResponseEntity<Map<Integer, String>> getPriorities() {
        return ResponseEntity.ok(dictionaryService.getPriorities());
    }
}
