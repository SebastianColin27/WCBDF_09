package com.upiiz.memorysecurity.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
public class FacturasController {

    @GetMapping("/listar")
    public String listar(){
        return "Lista de facturas - sin seguridad";
    }


    @GetMapping("/actualizar")
    public String actualizar(){
        return "Actualizado";
    }

    @GetMapping("/eliminar")
    public String eliminar(){
        return "Eliminar facturas con seguridad";
    }

    @GetMapping("/crear")
    public String crear(){
        return "Crear facturas con seguridad";
    }

}
