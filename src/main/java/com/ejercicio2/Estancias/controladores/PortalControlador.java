package com.ejercicio2.Estancias.controladores;

import com.ejercicio2.Estancias.entidades.Propietario;
import com.ejercicio2.Estancias.entidades.Usuario;
import com.ejercicio2.Estancias.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap modelo) {
        if (error != null) {
            modelo.addAttribute("error", "Usuario o password incorrecto");
        }
        if (logout != null) {
            modelo.addAttribute("logout", "Cerro sesión correctamente");
        }
        return "login.html";
    }

   @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_PROPIETARIO','ROLE_ADMIN')")
   @GetMapping("/inicio")
   public String inicio(){
       
       return "inicio.html";
   }
//    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_PROPIETARIO')")
//    @GetMapping("/inicio")
//    public String inicio(HttpSession session, ModelMap modelo) {
//        Usuario login = (Usuario) session.getAttribute("usuariosession");
//        if (login == null) {
//            modelo.put("error", "No hay usuario en la sesión");
//            return "login.html";
//        }
//            
//        return "inicio.html";
//    }
}
