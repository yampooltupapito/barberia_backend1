package com.cusi.barberia_backend.security;

import com.cusi.barberia_backend.entity.Admin;
import com.cusi.barberia_backend.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Admin no encontrado: " + nombreUsuario));

        return new User(
                admin.getNombreUsuario(),
                admin.getContraseña(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}