package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalidadDTO {
    private String id;
    private String name;
}
