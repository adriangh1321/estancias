/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejercicio2.Estancias.controladores;

import com.ejercicio2.Estancias.entidades.Cliente;
import com.ejercicio2.Estancias.enumeraciones.Rol;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.servicios.ClienteServicio;
import com.ejercicio2.Estancias.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente")
public class ClienteControlador {

    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public String cliente() {
        return "cliente.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/listarClientes")
    public String listarClientes(ModelMap modelo) {
        List<Cliente> clientes = clienteServicio.listarClientes();
        modelo.addAttribute("clientes", clientes);
        return "listarClientes.html";
    }

}
