package br.edu.atitus.api_java_springboot.services;

import br.edu.atitus.api_java_springboot.entities.PointEntity;
import br.edu.atitus.api_java_springboot.entities.UserEntity;
import br.edu.atitus.api_java_springboot.entities.VehicleEntity;
import br.edu.atitus.api_java_springboot.repositories.PointRepository;
import br.edu.atitus.api_java_springboot.repositories.VehicleRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {


    private final VehicleRepository repository;
    private final PointRepository pointRepository;

    public VehicleService(VehicleRepository repository, PointRepository pointRepository) {
        super();
        this.repository = repository;
        this.pointRepository = pointRepository;
    }

    //Metodo Save para criar e cadastrar objetos carro
    public VehicleEntity save (VehicleEntity car) throws Exception{

        if (car == null) {
            throw new Exception("Objeto Vazio");
        }

        if(car.getModelo() == null || car.getModelo().isEmpty()) {
            throw new Exception("Modelo não pode ser vazio");
        }

        if(car.getMarca() == null || car.getMarca().isEmpty()) {
            throw new Exception("Marca não pode ser vazio");
        }

        if(car.getPlaca() == null || car.getPlaca().isEmpty()) {
            throw new Exception("Placa não pode ser vazio");
        }

        //Verificação da informação da placa.

        String placa = car.getPlaca().toUpperCase();

        if (placa.length() != 7) {
            throw new Exception("Placa deve ter exatamente 7 caracteres");
        }

        long letras = placa.chars().filter(Character::isLetter).count();
        long numeros = placa.chars().filter(Character::isDigit).count();

        if (letras != 4 || numeros != 3) {
            throw new Exception("Placa deve conter exatamente 4 letras e 3 números");
        }

        car.setPlaca(placa);
        if(car.getAno() < 1900) {
            throw new Exception("Ano de fabricação não pode ser menor que 1900");
        }

        if(car.getCor() == null || car.getCor().isEmpty()) {
            throw new Exception("Cor não pode ser vazio");
        }

        UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        car.setUser(userAuth);

        List<PointEntity> point = pointRepository.findByUser(userAuth);
        if (point.isEmpty()) {
            throw new Exception("Usuário não possui ponto cadastrado");
        }
        car.setPoint(point.getLast());

        return repository.save(car);
    }

    //Basicamente esses dois métodos foram copiados do PointService, mas eu acho que funciona do mesmo jeito
    public void deleteByid (UUID id) throws Exception{

        var car = repository.findById(id).orElseThrow( () -> new Exception("Não existe carro cadastrado com esse ID") );

        UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!car.getUser().getId().equals(userAuth.getId())) {
            throw new Exception("Você não tem permisão para apagar");
        }
        repository.deleteById(id);
    }

    public List<VehicleEntity> findAll() {
        UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return repository.findByUser(userAuth);
    }
}
