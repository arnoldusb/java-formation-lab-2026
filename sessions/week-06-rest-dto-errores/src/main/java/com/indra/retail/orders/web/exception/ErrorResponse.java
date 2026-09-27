package com.indra.retail.orders.web.exception;

import java.util.List;

public record ErrorResponse(
        String timestamp,
        int status,
        List<String> errors) {

}
