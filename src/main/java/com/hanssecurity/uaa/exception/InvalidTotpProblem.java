package com.hanssecurity.uaa.exception;

import com.hanssecurity.uaa.config.Constants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

/**
 * @author hans
 */
public class InvalidTotpProblem extends AbstractThrowableProblem {

    private static final URI TYPE = URI.create(Constants.PROBLEM_BASE_URI + "/Invalid-totp-problem");

    public InvalidTotpProblem() {
        super(
                TYPE,
                "invalid code!!!!",
                Status.UNAUTHORIZED,
                "Totp code is not correct"
        );
    }
}

