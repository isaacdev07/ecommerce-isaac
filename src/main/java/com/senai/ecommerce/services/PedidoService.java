package com.senai.ecommerce.services;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.senai.ecommerce.dto.ItemPedidoDTO;
import com.senai.ecommerce.dto.PedidoDTO;
import com.senai.ecommerce.entities.ItemDoPedido;
import com.senai.ecommerce.entities.Pagamento;
import com.senai.ecommerce.entities.Pedido;
import com.senai.ecommerce.entities.Produto;
import com.senai.ecommerce.entities.StatusDoPedido;
import com.senai.ecommerce.entities.Usuario;
import com.senai.ecommerce.repositories.ItemDoPedidoRepository;
import com.senai.ecommerce.repositories.PagamentoRepository;
import com.senai.ecommerce.repositories.PedidoRepository;
import com.senai.ecommerce.repositories.ProdutoRepository;
import com.senai.ecommerce.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class PedidoService {

	@Autowired
	PedidoRepository pedidoRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	ProdutoRepository produtoRepository;

	@Autowired
	ItemDoPedidoRepository itemDoPedidoRepository;

	@Autowired
	PagamentoRepository pagamentoRepository;


	@Transactional
	public PedidoDTO inserir(PedidoDTO dto) {
		Pedido pedido = new Pedido();
		pedido.setMomento(Instant.now());
		pedido.setStatus(StatusDoPedido.AGUARDANDO_PAGAMENTO);

		Usuario user = usuarioRepository.getReferenceById(dto.getClienteId());
		pedido.setCliente(user);

		for(ItemPedidoDTO itemDTO : dto.getItems()) {
			Produto produto = produtoRepository.getReferenceById(itemDTO.getIdProduto());
			ItemDoPedido item = new ItemDoPedido(pedido, produto, itemDTO.getQuantidade(), itemDTO.getPreco());
			pedido.getItems().add(item);
		}

		pedido = pedidoRepository.save(pedido);
		itemDoPedidoRepository.saveAll(pedido.getItems());
		return new PedidoDTO(pedido);
	}

	public PedidoDTO findById(Long id) {
		Pedido pedido = pedidoRepository.findPedidoComItens(id);
		if (pedido == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado com o ID: " + id);
		}
		return new PedidoDTO(pedido);
	}

	@Transactional
	public PedidoDTO processarPagamento(Long pedidoId) {
		Optional<Pedido> pedidoOptional = pedidoRepository.findById(pedidoId);

		if (pedidoOptional.isPresent()) {
			Pedido pedido = pedidoOptional.get();
			if (StatusDoPedido.AGUARDANDO_PAGAMENTO.equals(pedido.getStatus())) {
				pedido.setStatus(StatusDoPedido.PAGO);
				Pedido pedidoPago = pedidoRepository.save(pedido);

				// Criar e salvar o pagamento associado ao pedido
				Pagamento pagamento = new Pagamento();
				pagamento.setMomento(Instant.now());
				pagamento.setPedido(pedidoPago); // Agora a associação deve funcionar

				pagamentoRepository.save(pagamento);

				return new PedidoDTO(pedidoPago);
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O pedido não está com o status 'AGUARDANDO_PAGAMENTO'. Status atual: " + pedido.getStatus());
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado com o ID: " + pedidoId);
		}
	}
}