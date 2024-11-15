package com.example.gomesrodris.archburgers.adapters.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FormaPagamentoTest {

    private FormaPagamentoDto formaPagamentoDto;


    @BeforeEach
    void setUp() {
        formaPagamentoDto = new FormaPagamentoDto("Forma1", "FormaPagamento Descricao");
    }

    @Test
    void validateFields() {
        assertThat(formaPagamentoDto.nome()).isEqualTo("Forma1");
        assertThat(formaPagamentoDto.descricao()).isEqualTo("FormaPagamento Descricao");
    }
}