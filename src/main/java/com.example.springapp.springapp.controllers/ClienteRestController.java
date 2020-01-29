package com.example.springapp.springapp.controllers;

import com.example.springapp.springapp.models.entity.Cliente;
import com.example.springapp.springapp.models.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {
    @Autowired
    private IClienteService clienteService;

    @GetMapping("/cliente")
    public List<Cliente> index(){
        return clienteService.findAll();
    }

    @GetMapping("/cliente/page/{page}")
    public Page<Cliente> index(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        return clienteService.findAll(pageable);
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Cliente cliente = null;
        Map<String, Object> response = new HashMap<>();
        try{
            cliente = clienteService.findBy(id);
        }catch (DataAccessException da){
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", da.getMessage().concat(": ").concat(da.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (cliente == null){
            response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
    }

    @PostMapping("/cliente")
    public  ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result)
    {
        Cliente newCliente = null;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(errr -> "El campo '" + errr.getField() + "' " + errr.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try{
            newCliente = clienteService.save(cliente);
        }catch (DataAccessException da){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", da.getMessage().concat(": ").concat(da.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente ha sido creado con exito");
        response.put("cliente", newCliente);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED );
    }

    @PutMapping("/cliente/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id){
        Cliente updateCliente = null;
        Map<String, Object> response = new HashMap<>();
        Cliente clienteActual = clienteService.findBy(id);
        if (clienteActual == null){
            response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(errr -> "El campo '" + errr.getField() + "' " + errr.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            clienteActual.setApellido(cliente.getApellido());
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setMail(cliente.getMail());
        }catch (DataAccessException da){
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", da.getMessage().concat(": ").concat(da.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        updateCliente = clienteService.save(clienteActual);
        response.put("mensaje", "El cliente ha sido actualizado con exito");
        response.put("cliente", updateCliente);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED );
    }

    @DeleteMapping("/cliente/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            clienteService.delete(id);
        }catch (DataAccessException da){
            response.put("mensaje", "Error al eliminar en la base de datos");
            response.put("error", da.getMessage().concat(": ").concat(da.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El cliente ha sido eliminado con exito");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
