import React from 'react';
import { render, screen } from '@testing-library/react';
import PaginaPrincipal from './PaginaPrincipal';
import { MemoryRouter } from 'react-router-dom';

describe('PaginaPrincipal (sin login)', () => {
  test('muestra saludo al superusuario', () => {
    render(
      <MemoryRouter>
        <PaginaPrincipal />
      </MemoryRouter>
    );

    //expect(screen.getByText(/Bienvenid@ al/i)).toBeInTheDocument();
    const heading2 = screen.getByRole('heading', { level: 2 });
    expect(heading2).toHaveTextContent(/Bienvenid@ al/i);
    expect(heading2).toHaveTextContent(/Sistema de Emisión de/i);
    expect(heading2).toHaveTextContent(/Licencias de Santa Fe/i);
  
  });

  test('renderiza los botones de navegación', () => {
    render(
      <MemoryRouter>
        <PaginaPrincipal />
      </MemoryRouter>
    );

    expect(screen.getByText(/Emitir Licencia/i)).toBeInTheDocument();
    expect(screen.getByText(/Dar de alta Titular/i)).toBeInTheDocument();
  });
});
