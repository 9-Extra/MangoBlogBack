package cn.mango.mangoblog.restserver;

import cn.mango.mangoblog.entity.Greeting;
import cn.mango.mangoblog.entity.ResultWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
public class GreetingController {
    private static final String template = "Hello, %s";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public ResultWrapper<Greeting> greeting(@RequestParam(value = "name", defaultValue = "World") String name){
        log.warn("Get request greeting");
        return new ResultWrapper<>(new Greeting(counter.incrementAndGet(), String.format(template, name)));
    }

}
