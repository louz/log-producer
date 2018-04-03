package io.jasonlu.logproducer.controller;

import io.jasonlu.logproducer.core.Printer;
import io.jasonlu.logproducer.domain.Progress;
import io.jasonlu.logproducer.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LogController {
    private Progress progress;

    @Autowired
    private LogService logService;

    @Autowired
    private Map<String, Printer> printers;

    @PostMapping(value = "/log-producer")
    public String produceLog(@RequestParam("logPerSecond") int logPerSecond,
                             @RequestParam("total") int total,
                             @RequestParam(value = "length", defaultValue = "512") int length,
                             @RequestParam(value = "type", defaultValue = "systemOutPrinter") String printerType) {
        progress = new Progress(total);
        Printer printer = printers.get(printerType);
        logService.produce(printer, logPerSecond, total, length, progress);
        return progress.getId();
    }

    @GetMapping(value = "/progress/{id}")
    public ResponseEntity<Progress> checkProgress(@PathVariable String id) {
        if (progress == null || !progress.getId().equals(id)) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(progress);
    }
}
