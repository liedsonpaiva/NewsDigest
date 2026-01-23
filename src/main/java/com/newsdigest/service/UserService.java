package com.newsdigest.service;

import com.newsdigest.domain.User;
import com.newsdigest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User cadastrarUsuario (String email, LocalTime horarioEnvio) {

        Optional<User> existente = userRepository.findByEmail(email);
        if (existente.isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        User user = new User(email, true, horarioEnvio);
        return userRepository.save(user);
    }

    public void desativarUsuario(Long userId) {
        User user = buscarPorId(userId);
        user.setAtivo(false);
        userRepository.save(user);
    }

    public void alterarHorarioEnvio(Long userId, LocalTime novoHorario) {
        User user = buscarPorId(userId);
        user.setHorarioEnvio(novoHorario);
        userRepository.save(user);
    }

    private User buscarPorId(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    public Optional<User> buscarUsuarioPorId(Long id) {
        return userRepository.findById(id)
                .filter(User::isAtivo);
    }

    public List<User> listarUsuarios() {
        return userRepository.findAll().stream()
                .filter(User::isAtivo)
                .toList();
    }
}
