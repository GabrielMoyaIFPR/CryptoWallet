package com.crypto.wallet.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.crypto.wallet.exception.BadResourceException;
import com.crypto.wallet.exception.ResourceAlreadyExistsException;
import com.crypto.wallet.exception.ResourceNotFoundException;
import com.crypto.wallet.model.Moeda;
import com.crypto.wallet.service.ServicoMoeda;

@RestController
@RequestMapping("/api")
public class ControleMoeda {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ServicoMoeda servicoMoeda;

	@GetMapping(value = "/moeda", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Moeda>> findAll(Pageable pageable){
		return ResponseEntity.ok(servicoMoeda.findAll(pageable));
	}
	
	@GetMapping(value = "/moeda/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Moeda> findMoedaById(@PathVariable long id) {
		try {
			Moeda moeda = servicoMoeda.findById(id);
			return ResponseEntity.ok(moeda);
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}
	
	@PostMapping(value = "/moeda")
	public ResponseEntity<Moeda> addMoeda(@RequestBody Moeda moeda) throws URISyntaxException {
		try {
			Moeda novaMoeda = servicoMoeda.save(moeda);
			return ResponseEntity.created(new URI("/api/usuario/" + novaMoeda.getId())).body(novaMoeda);
		} catch (ResourceAlreadyExistsException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PutMapping(value = "/moeda/{id}")
	public ResponseEntity<Moeda> updateUsuario(@RequestBody Moeda moeda, @PathVariable long id) {
		try {
			moeda.setId(id);
			servicoMoeda.update(moeda);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.notFound().build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@DeleteMapping(path = "/moeda/{id}")
	public ResponseEntity<Moeda> deleteUsuarioById(@PathVariable long id) {
		try {
			servicoMoeda.deleteById(id);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}
}
