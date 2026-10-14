## Fallo 1 — com.indra.catalog.suppliers.web.SupplierWebMapper null:
- **Síntoma**: todos test fallan con No qualifying bean of type 'com.indra.catalog.suppliers.web.SupplierWebMapper' available:
- **Causa raíz**: el `SupplierController` depende de `SupplierWebMapper`, pero la prueba solo cargaba el controlador y la dependencia no estaba registrada en el contexto.
- **Corrección**: añadimos el mapper al contexto del slice test con `@Import(SupplierWebMapper.class)` y dejamos la dependencia explícita del controller.
- **Prevención**: mantener cada dependencia web registrada en el contexto del test y validar el arranque del slice con `mvn test`.

## Fallo 2 — en la url falta /
- **Síntoma**: test3 Response header 'Location' expected:<http://localhost/api/suppliers/SUP-001> but was:<http://localhost/api/suppliersSUP-001>
- **Causa raíz**: `ServletUriComponentsBuilder.fromCurrentRequest.path("{id}")` pega automaticamente la url base mas el parametro no pone automatico el separador /
- **Corrección**: `path("{id}")` por agregar `pathSegment("{id}")` agrege el / automaticamente
- **Prevención**: mantener la prueba y usar pathSegment en vez de path cuando se requiere /.

## Fallo 3 — error interno
- **Síntoma**: test4 Status expected:<400> but was:<500>
- **Causa raíz**: aunque el `CreateSupplierRequest` tiene las validaciones no se estan aplicando el controller no sabe que las debe aplicar.
- **Corrección**: se agrego `@Valid` en el controller 
- **Prevención**: mantener la prueba y crear pruebas nuevas que validen los parametros

## Fallo 4 — sin/con contenido
- **Síntoma**: test5 Status expected:<204> but was:<200>
- **Causa raíz**: se esta retornado un ok(200 con cuerpo) para la eliminacion y esta espera un 204 ( ok pero sin cuerpo). por estandar ya que la eliminacion no requiere cuerpo.
- **Corrección**: en controller metodo eliminar `ResponseEntity.ok().build()` por `ResponseEntity.noContent().build()`
- **Prevención**: mantener la prueba actual, generar prueba para todos los codigo de retorno.


## Refactors aplicados
| Principio / práctica | Antes | Después | Test que lo respalda |
|---|---|---|---|
| DIP | `SupplierController` dependía de `SupplierServiceImpl` concreto y `SupplierServiceImpl` instanciaba su repositorio interno. | El controlador depende de la abstracción `SupplierService` y el servicio recibe `SupplierRepository` vía inyección. | `SupplierControllerTest` |
| SRP | La lógica de normalización estaba en el controller. | La validación y normalización de dominio se resuelven en `Supplier` y en el servicio. | `SupplierTest` |
| Dominio | `Supplier` permitía estados inválidos. | el contructor `public Supplier(String name, String taxId, String email)` y los setters validan nombre, NIT y email. | `SupplierTest` |
| Seguridad | El `ApiExceptionHandler` exponía `exception.toString()` en 500. | Se devuelve un mensaje genérico para no filtrar detalles internos. | `SupplierControllerTest` |
| Diseño web | El mapper era una dependencia de Spring cargada no siempre necesaria. | dejo de ser un componete y paso a ser una clase normal con new, en el test ya no se importa. `@Import(SupplierWebMapper.class)`| `SupplierControllerTest` |
