package com.ejercicio2.Estancias.servicios;

import com.ejercicio2.Estancias.entidades.Familia;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.repositorios.FamiliaRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FamiliaServicio {

    @Autowired
    private FamiliaRepositorio familiaRepositorio;

    @Transactional
    public Familia crearFamilia(String nombre, Integer edadMin, Integer edadMax, Integer numHijos) throws ErrorServicio {
        validarFamilia(nombre, edadMin, edadMax, numHijos);
        Familia familia = new Familia();
        familia.setNombre(nombre);
        familia.setEdadMin(edadMin);
        familia.setEdadMax(edadMax);
        familia.setNumHijos(numHijos);
        return familia;

    }

    public void validarFamilia(String nombre, Integer edadMin, Integer edadMax, Integer numHijos) throws ErrorServicio {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre de la familia está nulo o vacío");
        }
        if (edadMin == null) {
            throw new ErrorServicio("La edad minimo es nula ");
        }
        if (edadMin < 0) {
            throw new ErrorServicio("La edad minima es menor que cero");
        }
        if (edadMax == null) {
            throw new ErrorServicio("La edad maxima es nula");
        }
        if (edadMax < 0) {
            throw new ErrorServicio("La edad maxima es menor que cero");
        }
        if (edadMax < edadMin) {
            throw new ErrorServicio("La edad maxima es menor que la edad minima");
        }
        if (numHijos == null) {
            throw new ErrorServicio("El numero de hijos es nulo");
        }
        if (numHijos < 0) {
            throw new ErrorServicio("El numero de hijos es menor que cero");
        }

    }

    @Transactional
    public Familia modificarFamilia(String id, String nombre, Integer edadMin, Integer edadMax, Integer numHijos) throws ErrorServicio {
        
        if (id == null) {
            throw new ErrorServicio("El id no puede ser nulo");
        }
        validarFamilia(nombre, edadMin, edadMax, numHijos);
        Familia familia = familiaRepositorio.getById(id);
        if (familia != null) {
            familia.setNombre(nombre);
            familia.setEdadMin(edadMin);
            familia.setEdadMax(edadMax);
            familia.setNumHijos(numHijos);
            return familiaRepositorio.save(familia);
        } else {
            throw new ErrorServicio("La familia no fue encontrada");
        }
    }

    @Transactional(readOnly=true)
    public List<Familia> listarFamilia(){
        return familiaRepositorio.findAll();
    }
    
   
@Transactional(readOnly=true)
public Familia buscarFamiliaPorId(String id) {
//    if (id==null || id.trim().isEmpty()) {
//        throw new ErrorServicio("El id es nulo o está vacío");
//    }
    return familiaRepositorio.getById(id);
}
}
