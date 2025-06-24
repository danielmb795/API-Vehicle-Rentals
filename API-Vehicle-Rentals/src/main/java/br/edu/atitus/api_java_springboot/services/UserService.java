package br.edu.atitus.api_java_springboot.services;

import br.edu.atitus.api_java_springboot.entities.UserEntity;
import br.edu.atitus.api_java_springboot.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

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
        user.setName(user.getName().trim());

        if (user.getEmail() == null || user.getEmail().isEmpty())
            throw new Exception("Email inválido");
        user.setEmail(user.getEmail().trim().toLowerCase());

        // Validação do email (Estrutura, sintaxe[example@example.com] e pelo menos dois domínios)
        // Alteração na regex para garantir ao menos um ponto no domínio após o @
        Pattern emailRegex = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,}$"); // Alterado de {2,4} para {2,} para ser mais flexível, mas mantendo a estrutura de pelo menos dois domínios após o @
        if (!emailRegex.matcher(user.getEmail()).matches()) {
            throw new Exception("Formato de e-mail inválido. Deve conter @ e ao menos dois domínios (ex: usuario@dominio.com).");
        }


        if (user.getPassword() == null || user.getPassword().isEmpty() || user.getPassword().length() < 8)
            throw new Exception("Senha deve ter pelo menos 8 caracteres");

        String senha = user.getPassword();

        // Validação da senha (pelo menos 8 caracteres, maiúscula, minúscula e número)
        Pattern senhaForte = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
        if (!senhaForte.matcher(senha).matches()) {
            throw new Exception("A senha deve conter pelo menos 8 caracteres, incluindo uma letra maiúscula, uma minúscula e um número.");
        }


        if (user.getType() == null)
            throw new Exception("Tipo de usuário inválido");

        if (repository.existsByEmail(user.getEmail()))
            throw new Exception("Email já cadastrado");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário Não encontrado"));
        return user;
    }
}