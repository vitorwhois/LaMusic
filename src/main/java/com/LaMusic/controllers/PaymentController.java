package com.LaMusic.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LaMusic.dto.PaymentDTO;
import com.LaMusic.services.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

	 private final PaymentService service;

	    @GetMapping
	    public List<PaymentDTO> getAll() {
	        return service.findAll();
	    }

	    @GetMapping("/{id}")
	    public PaymentDTO getById(@PathVariable UUID id) {
	        return service.findById(id);
	    }

	    @PostMapping
	    public ResponseEntity<PaymentDTO> create(@RequestBody PaymentDTO dto) {
	        PaymentDTO created = service.create(dto);
	        return ResponseEntity.status(HttpStatus.CREATED).body(created);
	    }

	    @PutMapping("/{id}")
	    public PaymentDTO update(@PathVariable UUID id, @RequestBody PaymentDTO dto) {
	        return service.update(id, dto);
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> delete(@PathVariable UUID id) {
	        service.delete(id);
	        return ResponseEntity.noContent().build();
	    }
	
	
}
