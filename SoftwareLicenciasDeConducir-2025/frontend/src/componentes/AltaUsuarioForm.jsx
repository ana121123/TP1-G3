import React, { useState, useEffect } from "react";
import axios from "axios";
import "./AltaUsuarioForm.css";

const AltaUsuarioForm = () => {
  const [usuarios, setUsuarios] = useState([]);
  const [formData, setFormData] = useState({
    username: "",
    nombre: "",
    apellido: "",
    password: "",
    usernameSinModificar: "",
  });
  const [modoEdicion, setModoEdicion] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    cargarUsuarios();
  }, []);

  const cargarUsuarios = async () => {
    try {
      setLoading(true);
      const res = await axios.get("/api/usuarios/listar");
      setUsuarios(res.data);
    } catch (error) {
      alert("Error al cargar usuarios");
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (
      !formData.username.trim() ||
      !formData.nombre.trim() ||
      !formData.apellido.trim() ||
      (!formData.password.trim() && !modoEdicion)
    ) {
      alert("Por favor, complete todos los campos.");
      return;
    }

    try {
      setLoading(true);
      if (modoEdicion) {
       /* const dataToSend = {
            username: formData.username,
            nombre: formData.nombre,
            apellido: formData.apellido,
             password: formData.password === undefined ? "" : formData.password,
            usernameOriginal: formData.usernameOriginal
            };

            console.log("Datos que se envían:", dataToSend);*/
        await axios.put(`/api/usuarios/modificar`, {
          username: formData.username,
          nombre: formData.nombre,
          apellido: formData.apellido,
           password: formData.password === undefined ? "" : formData.password,
          usernameSinModificar: formData.usernameSinModificar
        });
        alert("Usuario modificado con éxito");
      } else {
        await axios.post("/api/usuarios", formData);
        alert("Usuario creado con éxito");
      }
      setFormData({username: "", nombre: "", apellido: "", password: "", usernameSinModificar: ""});
      setModoEdicion(false);
      cargarUsuarios();
    } catch (error) {
      alert("Error al guardar usuario");
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const editarUsuario = (usuario) => {
    setFormData({
      username: usuario.username,
      nombre: usuario.nombre,
      apellido: usuario.apellido,
      password: "",
       usernameSinModificar: usuario.username, 
    });
    setModoEdicion(true);
  };

  const eliminarUsuario = async (username) => {
    console.log("Username que se enviará para baja:", username); 
    if (!window.confirm("¿Seguro que desea eliminar este usuario?")) return;

    try {
      setLoading(true);
      await axios.put(`/api/usuarios/baja`, null, {
        params: { username }
    });
      alert("Usuario eliminado");
      cargarUsuarios();
    } catch (error) {
      alert("Error al eliminar usuario");
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const cancelarEdicion = () => {
    setModoEdicion(false);
    setFormData({username: "", nombre: "", apellido: "", password: "", usernameSinModificar: ""});
  };

  return (
    <div className="form-container">
        <h2>{modoEdicion ? "Modificar Usuario" : "Registrar Usuario Administrativo"}</h2>
        <form onSubmit={handleSubmit} className="formulario">
        <label htmlFor="username">Username:</label>
        <input
          type="text"
          name="username"
          id="username"
          value={formData.username}
          onChange={handleChange}
          placeholder="Nombre de usuario"
          autoComplete="username"
        />

        <label htmlFor="nombre">Nombre:</label>
        <input
          type="text"
          name="nombre"
          id="nombre"
          value={formData.nombre}
          onChange={handleChange}
          placeholder="Nombre"
        />

        <label htmlFor="apellido">Apellido:</label>
        <input
          type="text"
          name="apellido"
          id="apellido"
          value={formData.apellido}
          onChange={handleChange}
          placeholder="Apellido"
        />

        <label htmlFor="password">{modoEdicion ? "Nueva contraseña (opcional):" : "Contraseña:"}</label>
        <input
          type="password"
          name="password"
          id="password"
          value={formData.password}
          onChange={handleChange}
          placeholder={modoEdicion ? "Dejar vacío para no cambiar" : "Contraseña"}
          autoComplete={modoEdicion ? "new-password" : "current-password"}
        />

        <div className="botones-form">
          <button type="submit" disabled={loading}>
            {modoEdicion ? "Guardar Cambios" : "Registrar"}
          </button>
          {modoEdicion && (
            <button type="button" onClick={cancelarEdicion} disabled={loading} className="btn-cancelar">
              Cancelar
            </button>
          )}
        </div>
          </form>
      <hr />

      <h3>Usuarios existentes</h3>
      {loading && <p>Cargando...</p>}
      <ul className="lista-usuarios">
        {usuarios.length === 0 && !loading && <li>No hay usuarios registrados.</li>}
        {usuarios.map((usuario) => (
          <li key={usuario.id} className="usuario-item">
            <span>
              <b>{usuario.username}</b> - {usuario.nombre} {usuario.apellido}
            </span>
            <div>
              <button onClick={() => editarUsuario(usuario)} disabled={loading} className="btn-editar">
                Editar
              </button>
              <button onClick={() => eliminarUsuario(usuario.username)} disabled={loading} className="btn-eliminar">
                Eliminar
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default AltaUsuarioForm;
