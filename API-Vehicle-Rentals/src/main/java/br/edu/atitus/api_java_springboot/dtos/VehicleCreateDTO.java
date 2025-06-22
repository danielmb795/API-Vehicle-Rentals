package br.edu.atitus.api_java_springboot.dtos;

public record VehicleCreateDTO(String modelo, String marca, String placa, int ano, String cor) {
}
