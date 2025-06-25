package br.edu.atitus.api_java_springboot.controllers;

import br.edu.atitus.api_java_springboot.dtos.PointDTO;
import br.edu.atitus.api_java_springboot.entities.PointEntity;
import br.edu.atitus.api_java_springboot.services.PointService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/ws/point")
public class PointController {

    public final PointService service;


    public PointController(PointService service) {
        super();
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PointEntity> save(@RequestBody PointDTO dto )throws Exception{
        PointEntity point = new PointEntity();
        BeanUtils.copyProperties(dto,point);

        service.save(point);

        return ResponseEntity.status(201).body(point);
    }

    @GetMapping
    public ResponseEntity<List<PointEntity>> findAll(){
        var lista = service.findAll();

        return ResponseEntity.ok(lista);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody PointDTO dto) throws Exception {

        PointEntity existingPoint = service.findById(id);

        BeanUtils.copyProperties(dto, existingPoint);

        PointEntity updatedPoint = service.save(existingPoint);

        return ResponseEntity.ok(updatedPoint);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) throws Exception{
        service.deleteByid(id);

        return ResponseEntity.ok("Localização Excluida");
    }

}
