package com.sh.secondservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/second-service")
public class IndexController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Second service.";
    }

    @GetMapping("/message")
    public String message(@RequestHeader(name = "second-service-request") String header) {
        log.info(header);
        return header;
    }

    @GetMapping("/check")
    public String check() {
        return "this is a message from Second Service";
    }
}
