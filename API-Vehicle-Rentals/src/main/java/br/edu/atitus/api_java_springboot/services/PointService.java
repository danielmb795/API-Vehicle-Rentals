package br.edu.atitus.api_java_springboot.services;

import br.edu.atitus.api_java_springboot.entities.PointEntity;
import br.edu.atitus.api_java_springboot.entities.UserEntity;
import br.edu.atitus.api_java_springboot.repositories.PointRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PointService {

    private final PointRepository repository;

    public PointService(PointRepository repository) {
        super();
        this.repository = repository;
    }

    public PointEntity save(PointEntity point) throws Exception {

        if(point == null)
            throw new Exception("Objeto Vazio");

        if(point.getDescription() == null || point.getDescription().isEmpty())
            throw new Exception("Descrção invalida");
        point.setDescription(point.getDescription().trim());

        if(point.getLatitude() <- 90 || point.getLatitude() > 90)
            throw new Exception("Latitude invalida");

        if(point.getLongitude() <- 90 || point. getLongitude() > 90)
            throw new Exception("Longitude invalida");

        UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        point.setUser(userAuth);


        return repository.save(point);
    }

    public void deleteByid(UUID id) throws Exception{
        var point = repository.findById(id).orElseThrow(() -> new Exception("Não existe o ponto cadastrado com esse ID"));

        UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!point.getUser().getId().equals(userAuth.getId()))
            throw new Exception("Voce não tem permissão para apagar");
        repository.deleteById(id);
     }

     public List<PointEntity> findAll(){
         UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         return repository.findByUser(userAuth);
     }

    public PointEntity findById(UUID id) throws Exception {
        PointEntity point = repository.findById(id)
                .orElseThrow(() -> new Exception("Ponto não encontrado com o ID: " + id));

        UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!point.getUser().getId().equals(userAuth.getId())) {
            throw new Exception("Você não tem permissão para acessar esse ponto");
        }

        return point;
    }
}
