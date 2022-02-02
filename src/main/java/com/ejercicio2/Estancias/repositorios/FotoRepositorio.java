
package com.ejercicio2.Estancias.repositorios;

import com.ejercicio2.Estancias.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoRepositorio extends JpaRepository<Foto,String> {
    
}
