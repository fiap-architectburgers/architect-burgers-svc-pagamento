package com.example.gomesrodris.archburgers.domain.valueobjects;

import java.io.Serializable;

public record IdCliente(int id) implements Serializable {
    public IdCliente(Integer id) {
        this(id.intValue());
    }

}
