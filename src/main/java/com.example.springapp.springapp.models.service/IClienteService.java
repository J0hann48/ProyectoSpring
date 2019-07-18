package com.example.springapp.springapp.models.service;

import com.example.springapp.springapp.models.entity.Cliente;

import java.util.List;

public interface IClienteService {
    public List<Cliente> findAll();

    public Cliente save(Cliente cliente);

    public Cliente findBy(Long id);

    public void delete(Long id);
}
