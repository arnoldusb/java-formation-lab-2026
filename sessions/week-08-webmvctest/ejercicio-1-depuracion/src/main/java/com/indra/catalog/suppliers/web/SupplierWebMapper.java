package com.indra.catalog.suppliers.web;

import com.indra.catalog.suppliers.domain.Supplier;
import com.indra.catalog.suppliers.web.dto.CreateSupplierRequest;
import com.indra.catalog.suppliers.web.dto.SupplierResponse;
import org.springframework.stereotype.Component;

@Component
public class SupplierWebMapper {

    public Supplier toDomain(CreateSupplierRequest request) {
        Supplier supplier = new Supplier(request.name(),request.taxId(),request.email());  
        return supplier;
    }

    public SupplierResponse toResponse(Supplier supplier) {
        return new SupplierResponse(supplier.getId(), supplier.getName(), supplier.getTaxId(), supplier.getEmail());
    }
}
