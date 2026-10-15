# Estrategia de testing — Catálogo de productos

> Completa esta plantilla. Es el entregable que diferencia el nivel **Experto**: no basta con que los
> tests pasen, hay que justificar **qué se prueba dónde y por qué**.

## 1. Pirámide propuesta

| Nivel                 | Herramienta               | Qué valida                                             | Qué NO valida | Nº de tests | Tiempo aprox. |
|-----------------------|---------------------------|--------------------------------------------------------|---------------|-------------|---------------|
| Unitario (dominio)    | JUnit 5                   | invariantes del modelo (`Product`) y reglas de dominio | HTTP y seguridad | 1 | < 1.746 s |
| Unitario (aplicación) | JUnit 5 + Mockito         | comportamiento del caso de uso y coordinación con el repositorio | validación de MVC ni seguridad | 1 | 2.894 s |
| Slice web             | `@WebMvcTest` + `MockMvc` | traducción HTTP JSON, validación, status codes, headers y seguridad básica | persistencia real ni full context | 5 | 5.363 s |
| Integración           | `@SpringBootTest`         | cableado completo de la aplicación y configuración de seguridad | lógica de negocio detallada | 1 | 4.226 s |

## 2. Matriz de decisión

Para cada regla, marca en qué nivel se prueba y justifica.

| Regla / comportamiento              | Dominio | Aplicación | Slice web | Integración | Justificación |
|-------------------------------------|:-------:|:----------:|:---------:|:-----------:|---------------|
| El precio debe ser > 0              |   [x]   |            |           |             | La regla vive en `Product`, porque es una invariante del dominio y debe protegerse siempre, incluso si se reutiliza fuera del controller. |
| El precio debe ser > 0              |         |            |    [x]    |             | Exite tambien en el reques por eso se prueba en el web |
| El nombre vacío devuelve 400        |         |            |    [x]    |             | El controller valida la entrada HTTP con Bean Validation; la capa web es la responsable de traducir esa validación al código HTTP 400. |
| Producto inexistente devuelve 404   |         |            |    [x]    |             | El servicio lanza la excepción y pero el controller la transforma en 404. El slice web confirma el contrato HTTP y no depende de DB real. |
| Se genera un id al crear            |         |     [x]    |           |             | La generación del identificador es lógica de aplicación, donde se crea se valida. |
| `Location` apunta al recurso creado |         |            |    [x]    |             | El `Location` es parte de la respuesta HTTP, por lo que debe validarse en el slice web. |
| `DELETE` exige rol `ADMIN`          |         |            |    [x]    |             | La política de seguridad es de infraestructura/web y debe comprobarse en `MockMvc` con `httpBasic`. |
| Las capas quedan cableadas          |         |     [x]    |           |    [x]    | `@SpringBootTest` confirma el contexto completo y prueba el recorrido real servicio → repositorio en memoria (crear, leer y eliminar). |

## 3. Decisiones y trade-offs

- ¿Por qué `@MockBean` en el slice test y `@Mock` en el test del servicio?
  - En `@WebMvcTest` no queremos levantar la infraestructura real ni el repositorio; el objetivo es probar la traducción HTTP del controller. Por eso usamos `@MockBean` para `ProductService` y dejamos el resto de los beans web de Spring cargados por el slice. 
  - En el test del servicio usamos Mockito manual con `@Mock`/`mock()` porque el comportamiento que validamos es la coordinación entre el servicio y el repositorio, sin necesidad de iniciar Spring.

- ¿Qué pasa con el tiempo del pipeline si cada test de controller usa `@SpringBootTest`?
  - Cada test completo puede iniciar el contexto full de Spring, cargar seguridad, configuración, repositorios y etc.

- ¿Cómo aprovechas el caché de contexto de Spring entre clases de test? ¿Qué lo invalida?
  - No se.

- ¿Qué riesgos quedan sin cubrir y cómo los cubrirías (contract tests, tests de carga, etc.)?
  - Quedan sin cubrir escenarios reales de persistencia y concurrencia.

## 4. Mediciones

| Comando | Tests | Tiempo reportado por Surefire |
|---------|-------|-------------------------------|
| `mvn -pl ejercicio-2-construccion test -Dtest='ProductControllerTest*'` | 4 tests de slice web | 5.493s |
| `mvn -pl ejercicio-2-construccion verify` | smoke + unitarios + slice tests | 6.631 s |

### Resultado medido

los test estan por arriba de lo esperado.
