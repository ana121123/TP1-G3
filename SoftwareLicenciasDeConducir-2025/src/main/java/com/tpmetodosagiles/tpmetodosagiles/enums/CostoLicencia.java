package com.tpmetodosagiles.tpmetodosagiles.enums;

public enum CostoLicencia {
    A_5(40),
    A_4(30),
    A_3(25),
    A_1(20),
    B_5(40),
    B_4(30),
    B_3(25),
    B_1(20),
    C_5(47),
    C_4(35),
    C_3(30),
    C_1(23),
    D_5(59),
    D_4(44),
    D_3(39),
    D_1(29),
    E_5(59),
    E_4(44),
    E_3(39),
    E_1(29),
    F_5(40),
    F_4(30),
    F_3(25),
    F_1(20),
    G_5(40),
    G_4(30),
    G_3(25),
    G_1(20);

    private final int costo;

    CostoLicencia(int costo) {
        this.costo = costo;
    }

    public int getCosto() {
        return costo;
    }

    public static final int GASTOS_ADMINISTRATIVOS = 8;

    public static int obtenerCostoTotal(String clase, int aniosVigencia) {
        try {
            return CostoLicencia.valueOf(clase.toUpperCase() + "_" + aniosVigencia).getCosto() + GASTOS_ADMINISTRATIVOS;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Clase o años de vigencia no válidos");
        }
    }
}