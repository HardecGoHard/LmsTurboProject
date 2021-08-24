package com.Turbo.Lms.util;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.Map;
import java.util.stream.Collectors;

public class ControllerUtils {

    public static Map<String,String> getErros(BindingResult bindingResult){
        return bindingResult.getAllErrors().stream()
                .collect(Collectors.toMap(key ->
                                key.getCode() + "Error",
                        DefaultMessageSourceResolvable::getDefaultMessage)
                );
    }
}
