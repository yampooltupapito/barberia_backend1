package com.cusi.barberia_backend.repository;

import com.cusi.barberia_backend.entity.Admin;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//se adquieren los metodos para interactuar con bd
//repositorio para acceder y gestionar los datos de los administradores en la base de datos
public interface AdminRepository extends JpaRepository<Admin,Long> {
 // Busca un administrador por su nombre de usuario
 Optional<Admin>findByNombreUsuario(String nombreUsusario);
}
