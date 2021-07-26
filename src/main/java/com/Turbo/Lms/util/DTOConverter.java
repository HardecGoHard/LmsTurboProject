package com.Turbo.Lms.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DTOConverter {
    private static ModelMapper modelMapper;

    public static <D,T> D convertToDto(T base, Class<D> dtoClassType ) {
        return modelMapper.map(base, dtoClassType);
    }
    public static  <D,T> T convertToEntity(D dto, Class<T> baseClassType){
        return modelMapper.map(dto, baseClassType);
    }
}
