package com.ejercicio2.Estancias.servicios;

import com.ejercicio2.Estancias.entidades.Propietario;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ejercicio2.Estancias.repositorios.PropietarioRepositorio;

@Service
public class PropietarioServicio {

    @Autowired
    private PropietarioRepositorio propietarioRepositorio;

    @Transactional
    public Propietario crearPropietario(String nombre, String descripcion, String telefono) throws ErrorServicio {
        validarPropietario(nombre, descripcion, telefono);
        Propietario propietario = new Propietario();
        propietario.setNombre(nombre);
        propietario.setDescripcion(descripcion);
        propietario.setTelefono(telefono);

        return propietario;

    }

    public void validarPropietario(String nombre, String descripcion, String telefono) throws ErrorServicio {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre del propietario está nulo o vacío");
        }
        if (descripcion == null) {
            throw new ErrorServicio("La descripcion no puede ser nula");
        }
        if (descripcion.trim().isEmpty()) {
            throw new ErrorServicio("La descripción no puede esta vacía");
        }
        if (telefono == null) {
            throw new ErrorServicio("El número de telefono no debe ser nulo");
        }
        if (telefono.length() != 10) {
            throw new ErrorServicio("El número debe tener 10 digitos");
        }
    }

    @Transactional
    public Propietario modificarPropietario(String id, String nombre, String descripcion, String telefono) throws ErrorServicio {

        if (id == null) {
            throw new ErrorServicio("El id no puede ser nulo");
        }
        validarPropietario(nombre, descripcion, telefono);
        Propietario propietario = propietarioRepositorio.getById(id);
        if (propietario != null) {
            propietario.setNombre(nombre);
            propietario.setDescripcion(descripcion);
            propietario.setTelefono(telefono);
            return propietarioRepositorio.save(propietario);
        } else {
            throw new ErrorServicio("EL propietario no fue encontrado");
        }
    }

    @Transactional(readOnly = true)
    public List<Propietario> listarPropietario() {
        return propietarioRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Propietario buscarPropietarioPorId(String id) {
//    if (id==null || id.trim().isEmpty()) {
//        throw new ErrorServicio("El id es nulo o está vacío");
//    }
        return propietarioRepositorio.getById(id);
    }
}
