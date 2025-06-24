package br.edu.atitus.api_java_springboot.repositories;

import br.edu.atitus.api_java_springboot.entities.PointEntity;
import br.edu.atitus.api_java_springboot.entities.UserEntity;
import br.edu.atitus.api_java_springboot.entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, UUID> {
    List<VehicleEntity> findByUser(UserEntity user );
}
