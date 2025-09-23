# Documentación del proyecto

Este README tiene como objetivo describir el proyecto, su funcionamiento y cómo utilizarlo.  

## ¿Qué documentaríamos en este README?

En este README documentamos la información esencial para que cualquier persona pueda entender, ejecutar y contribuir al proyecto sin necesidad de asistencia adicional. Esto incluye:

- **Nombre y descripción del proyecto:** qué hace y cuál es su propósito.  
- **Requisitos e instalación:** dependencias necesarias, pasos para configurar el entorno y ejecutar la aplicación.  
- **Uso de la aplicación:** cómo correrla, ejemplos de ejecución, endpoints (si aplica), funcionalidades principales.  
- **Estructura del proyecto:** explicación de las carpetas y archivos más importantes.  
- **Flujo de desarrollo y GitFlow aplicado:** cómo se gestionan las ramas (`develop`, `feature`, `release`, `hotfix`) y el versionamiento.  
- **Autores y contacto:** quiénes desarrollaron el proyecto.  
- **Licencia:** si aplica, tipo de licencia del proyecto.  
- **Registro de cambios (changelog):** historial de versiones, funcionalidades nuevas y correcciones.  

## Pull Requests y revisión de cambios externos

Cuando un desarrollador externo realiza una modificación en el proyecto, es fundamental **entender exactamente qué cambio realizó, por qué y cómo afecta al proyecto**. Para esto se utilizan los **Pull Requests (PR) en GitHub**.

### Datos que se deberían solicitar al colaborador

Al momento de crear un Pull Request, es recomendable que el desarrollador complete y detalle la siguiente información:

1. **Título del PR**: resumen claro y conciso de la modificación.  
2. **Descripción del PR**: explicación detallada de los cambios realizados, incluyendo:  
   - Qué problema resuelve o qué funcionalidad agrega.  
   - Archivos y partes del código modificadas.  
   - Cómo probar los cambios y posibles efectos secundarios.  
3. **Referencias a issues o tareas**: si el cambio está asociado a un issue o ticket, incluir el número para facilitar el seguimiento.  
4. **Capturas o ejemplos** (si aplica): imágenes, logs o ejemplos que permitan entender mejor el cambio.  
5. **Checklist de pruebas**: confirmar que el código fue testeado y no rompe funcionalidades existentes.  

---

### Qué nos ofrece GitHub para ayudar en esto

GitHub facilita la revisión de cambios externos mediante varias herramientas integradas:

- **Comparación de ramas**: permite ver exactamente qué líneas de código se agregaron, modificaron o eliminaron.  
- **Comentarios en línea**: se puede dejar feedback directamente sobre líneas específicas del código.  
- **Checks automáticos / CI**: integración con pipelines que verifican que el código compile, pase tests y cumpla estándares de calidad.  
- **Historial de commits**: cada commit dentro del PR queda registrado con autor, fecha y mensaje descriptivo.  
- **Etiquetas y asignación**: se puede asignar revisores, añadir labels y vincular a issues, facilitando la trazabilidad del cambio.  

---

### Racional

- Solicitar información clara en el PR **permite entender rápidamente los cambios sin necesidad de contacto directo**.  
- GitHub centraliza la información y la mantiene versionada, lo que **facilita auditorías y seguimiento de cada modificación**.  
- Esto asegura que cualquier cambio externo sea **transparentemente revisable, testeable y documentado**, reduciendo errores y conflictos con el desarrollo principal.