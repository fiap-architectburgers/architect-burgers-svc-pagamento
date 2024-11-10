package com.example.gomesrodris.archburgers.di;

import com.example.gomesrodris.archburgers.adapters.datagateway.PagamentoGatewayImpl;
import com.example.gomesrodris.archburgers.adapters.datasource.MongoDatabaseConnection;
import com.example.gomesrodris.archburgers.adapters.datasource.PagamentoRepositoryNoSqlImpl;
import com.example.gomesrodris.archburgers.adapters.messaging.PagamentoEventMessagingGatewayImpl;
import com.example.gomesrodris.archburgers.adapters.pagamento.MercadoPagoGateway;
import com.example.gomesrodris.archburgers.adapters.presenters.QrCodePresenter;
import com.example.gomesrodris.archburgers.controller.PagamentoController;
import com.example.gomesrodris.archburgers.domain.datagateway.PagamentoEventMessagingGateway;
import com.example.gomesrodris.archburgers.domain.datagateway.PagamentoGateway;
import com.example.gomesrodris.archburgers.domain.datasource.PagamentoDataSource;
import com.example.gomesrodris.archburgers.domain.external.FormaPagamentoRegistry;
import com.example.gomesrodris.archburgers.domain.messaging.PagamentoEventMessaging;
import com.example.gomesrodris.archburgers.domain.usecases.PagamentoUseCases;
import com.example.gomesrodris.archburgers.domain.utils.Clock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DomainServiceBeans {

    @Bean
    Clock clock() {
        return new Clock();
    }

//    @Qualifier("noSQL")
//    PagamentoDataSource pagamentoDataSource;

    @Bean
    public PagamentoDataSource getPagamentoDatasource(MongoDatabaseConnection databaseConnection) {
        return new PagamentoRepositoryNoSqlImpl(databaseConnection);
    }

    @Bean
    public PagamentoGateway getPagamentoGateway(PagamentoDataSource pagamentoDataSource) {
        return new PagamentoGatewayImpl(pagamentoDataSource);
    }

    @Bean
    public PagamentoEventMessagingGateway pagamentoEventMessagingGateway(PagamentoEventMessaging pagamentoEventMessaging) {
        return new PagamentoEventMessagingGatewayImpl(pagamentoEventMessaging);
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
