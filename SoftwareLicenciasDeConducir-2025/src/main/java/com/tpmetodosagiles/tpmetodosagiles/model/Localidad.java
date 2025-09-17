package com.tpmetodosagiles.tpmetodosagiles.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "localidad")
public class Localidad {
    @Id
    private String id;
    private String code;
    private String name;

}
