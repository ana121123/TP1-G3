package com.tpmetodosagiles.tpmetodosagiles.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tpmetodosagiles.tpmetodosagiles.enums.*;

import lombok.Data;

@Document(collection = "claseLicencia")
@Data
public class Clase {
    @Id
    private String id;

    private ClaseLicencia categoria; // Enum en lugar de String
    private String subcategoria;
    private String titulo;
    private String descripcion;
}
