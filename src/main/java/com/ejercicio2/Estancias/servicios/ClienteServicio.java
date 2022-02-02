package com.ejercicio2.Estancias.servicios;

import com.ejercicio2.Estancias.entidades.Cliente;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.repositorios.ClienteRepositorio;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Transactional
    public Cliente crearCliente(
            String nombre,
            String calle,
            Integer numero,
            String codPostal,
            String ciudad,
            String pais
    ) throws ErrorServicio {
        validarCliente(nombre, calle, numero, codPostal, ciudad, pais);
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setCalle(calle);
        cliente.setNumero(numero);
        cliente.setCodPostal(codPostal);
        cliente.setCiudad(ciudad);
        cliente.setPais(pais);
        return cliente;
    }

    public void validarCliente(String nombre,
            String calle,
            Integer numero,
            String codPostal,
            String ciudad,
            String pais
    ) throws ErrorServicio {
        if (nombre == null) {
            throw new ErrorServicio("El nombre del cliente es nulo");
        }
        if (nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre está vacío");
        }
        if (calle == null) {
            throw new ErrorServicio("La calle es nula");
        }
        if (calle.trim().isEmpty()) {
            throw new ErrorServicio("La calle está vacíoa");
        }
        if (numero == null) {
            throw new ErrorServicio("El numero de la calle es nulo");
        }
        if (numero < 0) {
            throw new ErrorServicio("El numero de la calle es menor que cero");
        }
        if (codPostal == null) {
            throw new ErrorServicio("El codigo postal es nulo");
        }
        if (codPostal.trim().isEmpty()) {
            throw new ErrorServicio("El codigo postal está vacío");
        }

        if (ciudad == null) {
            throw new ErrorServicio("La ciudad esta nula");
        }
        if (ciudad.trim().isEmpty()) {
            throw new ErrorServicio("La ciudad está vacía");
        }

        if (pais == null) {
            throw new ErrorServicio("El país es nulo");
        }
        if (pais.trim().isEmpty()) {
            throw new ErrorServicio("El país está vacío");
        }

    }

    @Transactional
    public Cliente modificarCliente(String id,
            String nombre,
            String calle,
            Integer numero,
            String codPostal,
            String ciudad,
            String pais
    ) throws ErrorServicio {

        if (id == null) {
            throw new ErrorServicio("El id no puede ser nulo");
        }
        validarCliente(nombre, calle, numero, codPostal, ciudad, pais);

        Cliente cliente = clienteRepositorio.getById(id);

        if (cliente != null) {
            cliente.setNombre(nombre);
            cliente.setCalle(calle);
            cliente.setNumero(numero);
            cliente.setCodPostal(codPostal);
            cliente.setCiudad(ciudad);
            cliente.setPais(pais);
            return clienteRepositorio.save(cliente);
        } else {
            throw new ErrorServicio("El cliente no fue encontrado");
        }
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {
        return clienteRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscarClientePorId(String id) {
        return clienteRepositorio.getById(id);
    }
}
