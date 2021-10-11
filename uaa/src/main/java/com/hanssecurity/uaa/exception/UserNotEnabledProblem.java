package com.hanssecurity.uaa.exception;

import com.hanssecurity.uaa.config.Constants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

/**
 * @author hans
 */
public class UserNotEnabledProblem extends AbstractThrowableProblem {

    private static final URI TYPE = URI.create(Constants.PROBLEM_BASE_URI + "/user-not-enabled");

    public UserNotEnabledProblem() {
        super(
                TYPE,
                "UNAUTHORIZED!!!!",
                Status.UNAUTHORIZED,
                "The user is not activate"
        );
    }
}

