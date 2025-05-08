package com.senai.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.senai.ecommerce.dto.PagamentoRequestDTO;
import com.senai.ecommerce.dto.PedidoDTO;
import com.senai.ecommerce.services.PedidoService;

@RestController
@RequestMapping("pedido")
public class PedidoController {

	@Autowired
	PedidoService pedidoService;

	@PostMapping
	public ResponseEntity<PedidoDTO> inserir(@RequestBody PedidoDTO dto){
		dto = pedidoService.inserir(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@GetMapping("{id}")
	public ResponseEntity<PedidoDTO> Buscar(@PathVariable Long id){
		try {
			PedidoDTO pedidoDTO = pedidoService.findById(id);
			return ResponseEntity.ok(pedidoDTO);
		} catch (org.springframework.web.server.ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).build();
		}
	}

	@PostMapping("/pagar")
	public ResponseEntity<PedidoDTO> pagarPedido(@RequestBody PagamentoRequestDTO pagamentoRequestDTO) {
		try {
			PedidoDTO pedidoPago = pedidoService.processarPagamento(pagamentoRequestDTO.getPedidoId());
			return ResponseEntity.ok(pedidoPago);
		} catch (org.springframework.web.server.ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).build();
		}
	}
}