package io.jasonlu.logproducer;

import io.jasonlu.logproducer.domain.Progress;
import io.jasonlu.logproducer.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
// FIXME 移到一个controller中
public class Application {

    private Progress progress;

    @Autowired
    private LogService logService;

    @PostMapping(value = "/log-producer")
    public String produceLog(@RequestParam("logPerSecond") int logPerSecond,
                             @RequestParam("total") int total,
                             @RequestParam(value = "length", defaultValue = "512") int length) {
        progress = new Progress(total);
        logService.produce(logPerSecond, total, length, progress);
        return progress.getId();
    }

    @GetMapping(value = "/progress/{id}")
    public ResponseEntity<Progress> checkProgress(@PathVariable String id) {
        if (progress == null || !progress.getId().equals(id)) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(progress);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}