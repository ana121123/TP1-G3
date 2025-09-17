
import "./Login.css"; 

const Login = () => { 
  

  const handleSubmit = async (e) => {
    e.preventDefault();

    };

  
  return (

    <form onSubmit={handleSubmit} className="form-container">

      <div className="formulario-grid">
        {/* Columna izquierda */}
        <div className="login-info">
          <img src="/gob_santafe.png" alt="Santa Fe Logo" className="logo-sf" />
        </div>

        {/* Columna derecha */}
        <div className="login-formulario">
            
          <h2>Ingreso al Sistema</h2>
          <label htmlFor="usuario">Usuario:</label>
          <input
            type="text"
            placeholder="Usuario"
            name="usuario"
            required
          />
          <label htmlFor="clave">Contraseña:</label>
          <input
            type="password"
            placeholder="Contraseña"
            name="clave"
            required
          />
          <button type="submit">Ingresar</button>

          <div className="login-links">
            <a href="#">🔒 Recuperar clave</a>
          </div>
        </div>

      </div>
    </form>
    );
};

export default Login;
