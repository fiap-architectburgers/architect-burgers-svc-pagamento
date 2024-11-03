package com.example.gomesrodris.archburgers.domain.datagateway;

import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;

import java.util.List;

public interface ItemCardapioGateway {

    List<ItemPedido> findByPedido(int idPedido);

}
