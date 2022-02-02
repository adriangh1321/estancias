
package com.ejercicio2.Estancias.controladores;

import javax.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador {
    
    @GetMapping("/")
    public String index(){
        return "index.html";
    }
    
   @GetMapping("/login")
   public String login(@RequestParam(required=false)String error,@RequestParam(required=false)String logout,ModelMap modelo){
       if (error!=null) {
           modelo.addAttribute("error","Usuario o password incorrecto");
       }
       if (logout!=null) {
           modelo.addAttribute("logout","Cerro sesión correctamente");
       }
       return "login.html";
   }
   
   
   @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_FAMILIA')")
   @GetMapping("/inicio")
   public String inicio(){
       
       return "inicio.html";
   }
            
}
