package com.example.springapp.springapp.models.service;

import com.example.springapp.springapp.models.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService {
    public List<Cliente> findAll();

    public Page<Cliente> findAll(Pageable pageable);

    public Cliente save(Cliente cliente);

    public Cliente findBy(Long id);

    public void delete(Long id);
}
