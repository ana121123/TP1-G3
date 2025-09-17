import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Header from './componentes/Header';
import Footer from './componentes/Footer';
import PaginaPrincipal from './componentes/PaginaPrincipal';
import EmitirLicenciaForm from './componentes/EmitirLicenciaForm';
import AltaTitularForm from './componentes/AltaTitularForm';
import Licencia from './componentes/Licencia';
import LicenciasVigentes from './componentes/LicenciasVigentes';
import LicenciasExpiradas from './componentes/LicenciasExpiradas';
import LicenciaCopia from './componentes/LicenciaCopia';
import ModificarDatosLicencia from './componentes/ModificarDatosLicencia';
import Login from './componentes/Login';
import AltaUsuarioForm from './componentes/AltaUsuarioForm';

function App() {
  return (
    <div className="app-container">
      <Router>
        <Header />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<PaginaPrincipal />} />
            <Route path="/emitir-licencia" element={<EmitirLicenciaForm />} />
            <Route path="/alta-titular" element={<AltaTitularForm />} />
            <Route path="/licencia" element={<Licencia />} />
            <Route path="/licencias-vigentes" element={<LicenciasVigentes />} />
            <Route path="/licencias-expiradas" element={<LicenciasExpiradas />} />
            <Route path="/licencia-copia" element={<LicenciaCopia />} />
            <Route path="/modificar-datos-licencia" element={<ModificarDatosLicencia />} />
            <Route path="/login" element={<Login />} />
            <Route path="/alta-usuario" element={<AltaUsuarioForm />} />
          </Routes>
        </main>
        <Footer /> 
      </Router>
    </div>
  );
}

export default App;