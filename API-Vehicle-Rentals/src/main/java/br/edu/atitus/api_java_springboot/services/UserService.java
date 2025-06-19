package br.edu.atitus.api_java_springboot.services;

import br.edu.atitus.api_java_springboot.entities.UserEntity;
import br.edu.atitus.api_java_springboot.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        super();
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity save(UserEntity user) throws Exception{

        if (user == null)
            throw new Exception("Objeto não pode ser nulo");

        if (user.getUsername() == null || user.getUsername().isEmpty())
            throw new Exception("Nome inválido");
        user.setName(user.getUsername().trim());

        if (user.getEmail() == null || user.getEmail().isEmpty())
            throw new Exception("Email inválido");
        user.setEmail(user.getEmail().trim().toLowerCase());
        //TODO Validar o email (Estrutura, sintaxe[example@example.com]) => REGEX

        if (user.getPassword() == null || user.getPassword().isEmpty() || user.getPassword().length() < 8)
            throw new Exception("Senha inválida");
        //TODO Validar força da senha (caracteres maiúsculos, minisculos e numerais)

        if (user.getType() == null)
            throw new Exception("Tipo de usuário inválido");

        if (repository.existsByEmail(user.getEmail()))
            throw new Exception("Email já cadastrado");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        repository.save(user);

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário Não encontrado"));
        return user;
    }
}
