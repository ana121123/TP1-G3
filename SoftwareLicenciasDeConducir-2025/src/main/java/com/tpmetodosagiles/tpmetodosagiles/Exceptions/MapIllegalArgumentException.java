package com.tpmetodosagiles.tpmetodosagiles.Exceptions;

import java.util.HashMap;
import java.util.Map;

public class MapIllegalArgumentException extends IllegalArgumentException{
    private Map<String, String> errorMap;

    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    public MapIllegalArgumentException(String campo, String descripcion) {
        super();
        Map<String, String> errors = new HashMap<>();
        errors.put(campo, descripcion);
        this.errorMap = errors;
    }

    public MapIllegalArgumentException(Map<String, String> errorMap) {
        this.errorMap = errorMap;
    }

    
}
