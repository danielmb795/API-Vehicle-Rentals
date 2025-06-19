package br.edu.atitus.api_java_springboot.entities;

import jakarta.persistence.*;
import org.springframework.web.service.annotation.GetExchange;

import java.util.UUID;

@Entity
@Table(name="tb_vehicle")
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 30, nullable = false)
    private String modelo;

    @Column(length = 30, nullable = false)
    private String marca;

    @Column(length = 7, nullable = false)
    private String placa;

    @Column(length = 4, nullable = false)
    private int ano;


    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
}
