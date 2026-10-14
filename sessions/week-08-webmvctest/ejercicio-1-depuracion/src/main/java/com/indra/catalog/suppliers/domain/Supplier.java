package com.indra.catalog.suppliers.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Supplier {

    private String id;
    private String name;
    private String taxId;
    private String email;
    private String internalNotes;

    public Supplier(String name, String taxId, String email) {
        setName(name);
        setTaxId(taxId);
        setEmail(email);
        setInternalNotes(null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = normalizeName(name);
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = normalizeTaxId(taxId);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = normalizeEmail(email);
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = normalizeInternalNotes(internalNotes);
    }

    private String normalizeName(String candidate) {
        if (candidate == null || candidate.isBlank()) {
            throw new IllegalArgumentException("la razon social es obligatoria");
        }
        return candidate.trim().toUpperCase();
    }

    private String normalizeTaxId(String candidate) {
        if (candidate == null || candidate.isBlank()) {
            throw new IllegalArgumentException("el NIT es obligatorio");
        }
        String normalized = candidate.trim().replace(".", "");
        if (!normalized.matches("^[0-9]{9,11}-[0-9]$")) {
            throw new IllegalArgumentException("el NIT debe tener el formato 900123456-7");
        }
        return normalized;
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("el correo es obligatorio");
        }
        String normalized = email.trim();
        if (!normalized.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("el correo no es valido");
        }
        return normalized;
    }

    private String normalizeInternalNotes(String internalNotes) {
        if (internalNotes == null || internalNotes.isBlank()) {
            return "Creado vía API el " + LocalDateTime.now();
        }
        return internalNotes.trim();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Supplier other)) {
            return false;
        }
        return Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(taxId, other.taxId)
                && Objects.equals(email, other.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, taxId, email);
    }
}
