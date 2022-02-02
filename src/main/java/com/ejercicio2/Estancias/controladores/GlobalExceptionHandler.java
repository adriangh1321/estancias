
package com.ejercicio2.Estancias.controladores;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    
     @ExceptionHandler(MultipartException.class)
     public String handleError1(MultipartException e, RedirectAttributes r){
         r.addFlashAttribute("error", "El tamaño del archivo supero los 4MB");
         return "redirect:/index";
         
     }
}
