package com.example.gomesrodris.archburgers.di;

import com.example.gomesrodris.archburgers.adapters.pagamento.MercadoPagoGateway;
import com.example.gomesrodris.archburgers.adapters.presenters.QrCodePresenter;
import com.example.gomesrodris.archburgers.controller.PagamentoController;
import com.example.gomesrodris.archburgers.domain.datagateway.PagamentoEventMessagingGateway;
import com.example.gomesrodris.archburgers.domain.datagateway.PagamentoGateway;
import com.example.gomesrodris.archburgers.domain.external.FormaPagamentoRegistry;
import com.example.gomesrodris.archburgers.domain.usecases.PagamentoUseCases;
import com.example.gomesrodris.archburgers.domain.utils.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DomainServiceBeans {

    @Bean
    Clock clock() {
        return new Clock();
    }


    @Bean
    public FormaPagamentoRegistry formaPagamentoRegistry(MercadoPagoGateway mercadoPagoGateway) {
        return new FormaPagamentoRegistry(List.of(
                mercadoPagoGateway
        ));
    }

    @Bean
    public PagamentoUseCases pagamentoUseCases(FormaPagamentoRegistry formaPagamentoRegistry,
                                               PagamentoGateway pagamentoGateway,
                                               PagamentoEventMessagingGateway pagamentoEventMessagingGateway,
                                               Clock clock) {
        return new PagamentoUseCases(formaPagamentoRegistry, pagamentoGateway, pagamentoEventMessagingGateway,
                clock);
    }

    @Bean
    public PagamentoController pagamentoController(FormaPagamentoRegistry formaPagamentoRegistry,
                                                   PagamentoGateway pagamentoGateway,
                                                   PagamentoEventMessagingGateway pagamentoEventMessagingGateway,
                                                   Clock clock) {
        return new PagamentoController(formaPagamentoRegistry, pagamentoGateway, pagamentoEventMessagingGateway,
                clock);
    }

    @Bean
    public QrCodePresenter qrCodePresenter() {
        return new QrCodePresenter();
    }
}
