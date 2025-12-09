package org.pet.backendpetshelter.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test-sentry")
@CrossOrigin
public class SentryTestController {

    @GetMapping
    public String testSentry() {
        throw new RuntimeException("Backend Sentry Test Error!");
    }

    @GetMapping("/divide-by-zero")
    public int divideByZero() {
        return 1 / 0; // ArithmeticException
    }
}
