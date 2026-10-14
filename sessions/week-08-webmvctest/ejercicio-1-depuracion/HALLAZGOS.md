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