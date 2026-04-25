package com.cusi.barberia_backend.repository;

import com.cusi.barberia_backend.entity.Admin;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//se adquieren los metodos para interactuar con bd
public interface AdminRepository extends JpaRepository<Admin,Long> {
 Optional<Admin>findByNombreUsuario(String nombreUsusario);
}
