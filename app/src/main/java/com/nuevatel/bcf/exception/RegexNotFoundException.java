package com.nuevatel.bcf.exception;

import com.nuevatel.common.exception.OperationException;

/**
 * Created by asalazar on 6/11/15.
 */
public class RegexNotFoundException extends OperationException {

    public RegexNotFoundException(Integer regexId) {
        super(String.format("Regex not found %s", regexId));
    }
}
