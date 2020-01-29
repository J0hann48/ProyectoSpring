package com.example.springapp.springapp.models.dao;

import com.example.springapp.springapp.models.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface IClienteDao extends JpaRepository<Cliente, Long> {
}
