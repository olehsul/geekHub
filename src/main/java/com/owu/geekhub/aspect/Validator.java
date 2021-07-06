package com.owu.geekhub.aspect;

import com.owu.geekhub.exception.GenericGeekhubException;
import com.owu.geekhub.exception.UserInvalidException;

import java.util.List;

public interface Validator {

    boolean supports(Class<?> clazz);
    <U extends GenericGeekhubException> void validate(Object target) throws UserInvalidException;

}
