
package com.ejercicio2.Estancias.repositorios;

import com.ejercicio2.Estancias.entidades.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropietarioRepositorio extends JpaRepository<Propietario,String> {
    
}
