package com.sztus.lib.back.end.basic.controller;

import com.sztus.lib.back.end.basic.type.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author AustinWang
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.ok("hello");
    }

    @GetMapping("/hello2")
    public Result<String> hello2() {
        return Result.ok("hello2");
    }
}
