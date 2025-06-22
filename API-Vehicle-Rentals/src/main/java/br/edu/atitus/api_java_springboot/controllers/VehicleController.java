package br.edu.atitus.api_java_springboot.controllers;


import br.edu.atitus.api_java_springboot.dtos.VehicleCreateDTO;
import br.edu.atitus.api_java_springboot.entities.VehicleEntity;
import br.edu.atitus.api_java_springboot.services.VehicleService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ws/")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service){
        super();
        this.service = service;
    }
    
    @PostMapping("/cadastro-veiculos")
    public ResponseEntity<VehicleEntity> cadastroVeiculos(@RequestBody VehicleCreateDTO dto)throws Exception{

        VehicleEntity vehicle = new VehicleEntity();
        BeanUtils.copyProperties(dto,vehicle);

        service.save(vehicle);

        return ResponseEntity.status(201).body(vehicle);
    }

    @GetMapping
    public ResponseEntity<List<VehicleEntity>> listaVeiculos(){
        var lista = service.findAll();

        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVehicles(@PathVariable UUID id) throws Exception{

        return ResponseEntity.ok("Localização Excluida");
    }


}
