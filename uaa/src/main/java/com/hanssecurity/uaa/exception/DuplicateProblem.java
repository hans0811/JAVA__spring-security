package com.hanssecurity.uaa.exception;

import com.hanssecurity.uaa.config.Constants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

/**
 * @author hans
 */
public class DuplicateProblem extends AbstractThrowableProblem {

    private static final URI TYPE = URI.create(Constants.PROBLEM_BASE_URI + "/duplicate");

    public DuplicateProblem(String message) {
        super(TYPE, "Duplicate data", Status.CONFLICT, message);
    }
}
