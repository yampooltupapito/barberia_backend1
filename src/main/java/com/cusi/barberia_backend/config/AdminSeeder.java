package com.cusi.barberia_backend.config;

import com.cusi.barberia_backend.entity.Admin;
import com.cusi.barberia_backend.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Se ejecuta UNA SOLA VEZ al arrancar la aplicación.
 * Si no existe ningún admin en la BD, crea uno por defecto.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder implements ApplicationRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setNombreUsuario("admin");
            admin.setContraseña(passwordEncoder.encode("admin123"));
            adminRepository.save(admin);
            log.warn("===================================================");
            log.warn("  Admin creado: usuario='admin' / pass='admin123'  ");
            log.warn("  Cambia la contraseña antes de ir a producción!   ");
            log.warn("===================================================");
        }
    }
}