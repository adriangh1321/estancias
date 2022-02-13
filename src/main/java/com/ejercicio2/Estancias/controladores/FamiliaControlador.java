
package com.ejercicio2.Estancias.controladores;

import com.ejercicio2.Estancias.entidades.Familia;
import com.ejercicio2.Estancias.servicios.FamiliaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/familia")
public class FamiliaControlador {
    @Autowired
    private FamiliaServicio familiaServicio;
    
    @GetMapping
    public String familia(){
        return "familia.html";
    }
    
    @GetMapping("/listarFamilias")
    public String listarFamilias(ModelMap modelo){
    List<Familia> familias=familiaServicio.listarFamilia();
    modelo.put("familias",familias);
    return "listarFamilias.html";
}

    

    
}
