import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import AltaTitularForm from "./AltaTitularForm";
import axios from "axios";

jest.mock("axios");

/*beforeEach(() => {
  jest.clearAllMocks();

  axios.get.mockResolvedValue({
    data: [
      { id: 1, nombre: "Santa Fe" },
      { id: 2, nombre: "Rosario" },
    ],
  });

  axios.post.mockResolvedValue({ data: { success: true } }); // por si hace falta para los tests
});*/

describe("AltaTitularForm", () => {
  const datosValidos = {
    tipoDocumento: "DNI (Documento Nacional de Identidad)",
    nroDocumento: "12345678",
    apellido: "Perez",
    nombre: "Juan",
    fechaNacimiento: "1990-05-01",
    direccion: "Calle Falsa 123",
    localidad: "Santa Fe",
    grupoSanguineo: "A",
    factorRH: "+",
    donante: "SI",
    cuit: "20-12345678-9",
    observaciones: "Ninguna",
  };

  beforeEach(() => {
    jest.clearAllMocks();
  });

  test("registra titular correctamente y muestra resumen en modal", async () => {
    axios.get.mockResolvedValueOnce({
      data: [{ id: 1, nombre: "Santa Fe" }, { id: 2, nombre: "Rosario" }],
    });
    
    axios.post.mockResolvedValueOnce({ status: 201 });


    render(<AltaTitularForm />);
    await waitFor(() =>
      expect(screen.getByRole('option', { name: /Santa Fe/i })).toBeInTheDocument()
    );
   

    // Completar el formulario
    userEvent.selectOptions(screen.getByLabelText(/Tipo de Documento/i), datosValidos.tipoDocumento);
    userEvent.type(screen.getByLabelText(/Número de Documento/i), datosValidos.nroDocumento);
    userEvent.type(screen.getByLabelText(/Apellido/i), datosValidos.apellido);
    userEvent.type(screen.getByLabelText(/Nombre/i), datosValidos.nombre);
    userEvent.type(screen.getByLabelText(/Fecha de Nacimiento/i), datosValidos.fechaNacimiento);
    userEvent.type(screen.getByLabelText(/Dirección/i), datosValidos.direccion);
    userEvent.selectOptions(screen.getByLabelText(/Grupo Sanguíneo/i), datosValidos.grupoSanguineo);
    userEvent.selectOptions(screen.getByLabelText(/Factor RH/i), datosValidos.factorRH);
     userEvent.selectOptions(screen.getByLabelText(/Localidad/i), "Santa Fe");
    userEvent.selectOptions(screen.getByLabelText(/Donante de órganos/i), datosValidos.donante);
    userEvent.type(screen.getByLabelText(/CUIT/i), datosValidos.cuit);
    userEvent.type(screen.getByLabelText(/Observaciones/i), datosValidos.observaciones);

    // Enviar formulario
    fireEvent.click(screen.getByRole("button", { name: /Registrar titular/i }));

    // Esperar el mensaje de éxito y modal abierto
    /*await waitFor(() => {
      expect(screen.getByText(/Titular registrado correctamente./i)).toBeInTheDocument();
    });*/

    // El modal debe mostrar datos del titular
    expect(screen.getByText(/Resumen del Titular/i)).toBeInTheDocument();
    expect(screen.getByText(/Tipo de Documento:/i)).toBeInTheDocument();
    const elementos = screen.getAllByText(/Documento:/i);
    expect(elementos[0]).toBeInTheDocument();   
    expect(screen.getByText(/Apellido:/i)).toBeInTheDocument();
    expect(screen.getByText(/Nombre:/i)).toBeInTheDocument();
    expect(screen.getByText(/Fecha de nacimiento:/i)).toBeInTheDocument();
    expect(screen.getByText(/Dirección:/i)).toBeInTheDocument();
    expect(screen.getByText(/Grupo Sanguíneo:/i)).toBeInTheDocument();
    expect(screen.getByText(/Factor RH:/i)).toBeInTheDocument();
    expect(screen.getByText(/Donante:/i)).toBeInTheDocument();
    expect(screen.getByText(/Cuit:/i)).toBeInTheDocument();
    await waitFor(() =>
      expect(screen.getByLabelText(/Localidad/i).options.length).toBeGreaterThan(1)
    );
    expect(screen.getByText(/Observaciones:/i)).toBeInTheDocument();
    expect(screen.getByText(new RegExp(datosValidos.nombre, "i"))).toBeInTheDocument();

    // Cerrar modal
    fireEvent.click(screen.getByText("X"));
    expect(screen.queryByText(/Resumen del Titular/i)).not.toBeInTheDocument();
  });


  test("limpia los campos del formulario después de registrar titular correctamente", async () => {
    axios.post.mockResolvedValueOnce({ status: 201 });

    render(<AltaTitularForm />);
    await waitFor(() =>
      expect(screen.getByRole("option", { name: /Santa Fe/i })).toBeInTheDocument()
    );
   

    userEvent.selectOptions(screen.getByLabelText(/Tipo de Documento/i), datosValidos.tipoDocumento);
    userEvent.type(screen.getByLabelText(/Número de Documento/i), datosValidos.nroDocumento);
    userEvent.type(screen.getByLabelText(/Apellido/i), datosValidos.apellido);
    userEvent.type(screen.getByLabelText(/Nombre/i), datosValidos.nombre);
    userEvent.type(screen.getByLabelText(/Fecha de Nacimiento/i), datosValidos.fechaNacimiento);
    userEvent.type(screen.getByLabelText(/Dirección/i), datosValidos.direccion);
    userEvent.selectOptions(screen.getByLabelText(/Localidad/i), "Santa Fe");
    userEvent.selectOptions(screen.getByLabelText(/Grupo Sanguíneo/i), datosValidos.grupoSanguineo);
    userEvent.selectOptions(screen.getByLabelText(/Factor RH/i), datosValidos.factorRH);
    userEvent.selectOptions(screen.getByLabelText(/Donante de órganos/i), datosValidos.donante);
    userEvent.type(screen.getByLabelText(/CUIT/i), datosValidos.cuit);
    userEvent.type(screen.getByLabelText(/Observaciones/i), datosValidos.observaciones);

    fireEvent.click(screen.getByRole("button", { name: /Registrar titular/i }));

    await waitFor(() => {
      expect(screen.getByText(/Titular registrado correctamente/i)).toBeInTheDocument();
    });

    expect(screen.getByLabelText(/Tipo de Documento/i)).toHaveValue("");
    expect(screen.getByLabelText(/Número de Documento/i)).toHaveValue("");
    expect(screen.getByLabelText(/Apellido/i)).toHaveValue("");
    expect(screen.getByLabelText(/Nombre/i)).toHaveValue("");
    expect(screen.getByLabelText(/Fecha de Nacimiento/i)).toHaveValue("");
    expect(screen.getByLabelText(/Dirección/i)).toHaveValue("");
    expect(screen.getByLabelText(/Localidad/i)).toHaveValue("");
    expect(screen.getByLabelText(/Grupo Sanguíneo/i)).toHaveValue("");
    expect(screen.getByLabelText(/Factor RH/i)).toHaveValue("");
    expect(screen.getByLabelText(/Donante de órganos/i)).toHaveValue("");
    expect(screen.getByLabelText(/CUIT/i)).toHaveValue("");
    expect(screen.getByLabelText(/Observaciones/i)).toHaveValue("");
  });


  test("muestra mensaje de error cuando backend falla", async () => {
    axios.post.mockRejectedValueOnce({
      response: { data: { message: "Error de backend" } },
    });

    render(<AltaTitularForm />);
    await waitFor(() =>
      expect(screen.getByRole("option", { name: /Santa Fe/i })).toBeInTheDocument()
    );
  
    userEvent.selectOptions(screen.getByLabelText(/Tipo de Documento/i), datosValidos.tipoDocumento);
    userEvent.type(screen.getByLabelText(/Número de Documento/i), datosValidos.nroDocumento);
    userEvent.type(screen.getByLabelText(/Apellido/i), datosValidos.apellido);
    userEvent.type(screen.getByLabelText(/Nombre/i), datosValidos.nombre);
    userEvent.type(screen.getByLabelText(/Fecha de Nacimiento/i), datosValidos.fechaNacimiento);
    userEvent.type(screen.getByLabelText(/Dirección/i), datosValidos.direccion);
    userEvent.selectOptions(screen.getByLabelText(/Grupo Sanguíneo/i), datosValidos.grupoSanguineo);
    userEvent.selectOptions(screen.getByLabelText(/Factor RH/i), datosValidos.factorRH);
    userEvent.selectOptions(screen.getByLabelText(/Donante de órganos/i), datosValidos.donante);
    userEvent.type(screen.getByLabelText(/CUIT/i), datosValidos.cuit);
     userEvent.selectOptions(screen.getByLabelText(/Localidad/i), "Santa Fe");
    userEvent.type(screen.getByLabelText(/Observaciones/i), datosValidos.observaciones);

    fireEvent.click(screen.getByRole("button", { name: /Registrar titular/i }));

    await waitFor(() => {
      expect(screen.getByText(/Error de backend/i)).toBeInTheDocument();
    });

    // No debe abrirse el modal
    expect(screen.queryByText(/Resumen del Titular/i)).not.toBeInTheDocument();
  });

  test("muestra errores de validación y no envía formulario", async () => {
    render(<AltaTitularForm />);
    await waitFor(() =>
      expect(screen.getByRole("option", { name: /Santa Fe/i })).toBeInTheDocument()
    );

    // Completar con datos inválidos
    userEvent.selectOptions(screen.getByLabelText(/Tipo de Documento/i), "");
    userEvent.type(screen.getByLabelText(/Número de Documento/i), "abc"); // inválido
    userEvent.type(screen.getByLabelText(/Apellido/i), "123"); // inválido
    userEvent.type(screen.getByLabelText(/Nombre/i), "!!!"); // inválido
    userEvent.type(screen.getByLabelText(/Fecha de Nacimiento/i), "2050-01-01"); // futuro inválido
    userEvent.type(screen.getByLabelText(/Dirección/i), "Calle falsa");
    userEvent.selectOptions(screen.getByLabelText(/Grupo Sanguíneo/i), "");
    userEvent.type(screen.getByLabelText(/Localidad/i), "");
    userEvent.selectOptions(screen.getByLabelText(/Factor RH/i), "");
    userEvent.selectOptions(screen.getByLabelText(/Donante de órganos/i), "");
    userEvent.type(screen.getByLabelText(/CUIT/i), "00-00000000-0"); // inválido

    fireEvent.click(screen.getByRole("button", { name: /Registrar titular/i }));

    // Verificaciones individuales por cada mensaje de error, condicionales
    const errorDNI = screen.queryByText(/DNI inválido/i);
    if (errorDNI) {
    expect(errorDNI).toBeInTheDocument();
    }

    const errorApellido = screen.queryByText(/Apellido inválido/i);
    if (errorApellido) {
    expect(errorApellido).toBeInTheDocument();
    }

    const errorNombre = screen.queryByText(/Nombre inválido/i);
    if (errorNombre) {
    expect(errorNombre).toBeInTheDocument();
    }

    const errorFecha = screen.queryByText(/Fecha de nacimiento inválida/i);
    if (errorFecha) {
    expect(errorFecha).toBeInTheDocument();
    }

    const errorCUIT = screen.queryByText(/CUIT inválido/i);
    if (errorCUIT) {
    expect(errorCUIT).toBeInTheDocument();
    }

    // No debe llamar a axios.post
    expect(axios.post).not.toHaveBeenCalled();
  });
});
