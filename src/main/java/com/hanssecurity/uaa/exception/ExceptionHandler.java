package com.hanssecurity.uaa.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

/**
 * @author hans
 */

@ControllerAdvice
public class ExceptionHandler implements ProblemHandling {
    // when production set to false
    @Override
    public boolean isCausalChainsEnabled() {
        return false;
    }
}
