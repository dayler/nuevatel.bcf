package com.nuevatel.bcf.core.exception;

import com.nuevatel.common.exception.OperationException;

/**
 * When unit is not found in the database.
 *
 * @author Ariel Salazar
 */
public final class UnitNotFoundException extends OperationException {

    public UnitNotFoundException(String name) {
        super(String.format("Unit not found %s", name));
    }
}
