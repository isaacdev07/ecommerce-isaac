package com.senai.ecommerce.dto;

public class PagamentoRequestDTO {
    private Long pedidoId;

    public PagamentoRequestDTO() {
    }

    public PagamentoRequestDTO(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }
}