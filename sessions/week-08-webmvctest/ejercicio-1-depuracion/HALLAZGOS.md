## Fallo 1 — com.indra.catalog.suppliers.web.SupplierWebMapper null:
- **Síntoma**: todos test fallan con No qualifying bean of type 'com.indra.catalog.suppliers.web.SupplierWebMapper' available:
- **Causa raíz**: el `SupplierController` depende de `SupplierWebMapper`, pero la prueba solo cargaba el controlador y la dependencia no estaba registrada en el contexto.
- **Corrección**: añadimos el mapper al contexto del slice test con `@Import(SupplierWebMapper.class)` y dejamos la dependencia explícita del controller.
- **Prevención**: mantener cada dependencia web registrada en el contexto del test y validar el arranque del slice con `mvn test`.