package com.tpmetodosagiles.tpmetodosagiles.Controller;

import java.time.LocalDate;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tpmetodosagiles.tpmetodosagiles.Exceptions.AuthenticationException;
import com.tpmetodosagiles.tpmetodosagiles.Exceptions.MapIllegalArgumentException;

@RestControllerAdvice
public class ControllerAdvice {

    //mensaje formato datos incorrectos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validacionFormatoTitular(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    //mensaje titular ya existente
    @ExceptionHandler(MapIllegalArgumentException.class)
    public ResponseEntity<?> validacionUnicidadTitular(MapIllegalArgumentException ex){
        return new ResponseEntity<>(ex.getErrorMap(), HttpStatus.BAD_REQUEST);
    }

    //mensaje datos invalidos para el parseo (string --> localdate, Boolean)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage = "Error en el formato de la solicitud.";

        Throwable mostSpecificCause = ex.getMostSpecificCause();
        if (mostSpecificCause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) mostSpecificCause;
            if (ife.getTargetType() != null && LocalDate.class.isAssignableFrom(ife.getTargetType())) {
                errorMessage = String.format("Formato de fecha inválido para el campo '%s'. Se espera un formato como YYYY-MM-DD.", ife.getPath().get(0).getFieldName());
            } else {
                errorMessage = String.format("Valor '%s' no válido para el tipo '%s'.", ife.getValue(), ife.getTargetType().getSimpleName());
            }
        } else {
            errorMessage = "La solicitud no pudo ser leída o parseada. Revise el formato JSON.";
        }

        errors.put("globalError", errorMessage);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

   /* //no pasa autenticacion(jwt)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> validacionToken(Exception ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("GenericError:error desconocido", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }*/
}
