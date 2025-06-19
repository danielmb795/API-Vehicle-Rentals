package br.edu.atitus.api_java_springboot.controllers;

import br.edu.atitus.api_java_springboot.components.JWTUtils;
import br.edu.atitus.api_java_springboot.dtos.SignInDTO;
import br.edu.atitus.api_java_springboot.dtos.SignupDTO;
import br.edu.atitus.api_java_springboot.entities.UserEntity;
import br.edu.atitus.api_java_springboot.entities.UserType;
import br.edu.atitus.api_java_springboot.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService service;
    private final AuthenticationConfiguration authConfig;

    public AuthController(UserService service, AuthenticationConfiguration authConfig) {
        super();
        this.service = service;
        this.authConfig = authConfig;
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody SignInDTO signin)  {

        try {
            authConfig.getAuthenticationManager().
                    authenticate(new UsernamePasswordAuthenticationToken(signin.email().toLowerCase(), signin.password()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(JWTUtils.generateToken(signin.email().toLowerCase()));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserEntity> signup(@RequestBody SignupDTO dto) throws Exception{
        // Entidade criada
        UserEntity user = new UserEntity();

        // Propriedades da DTO copiadas para a entidade
        BeanUtils.copyProperties(dto, user);

        //Valores não presentes na DTO setados
        user.setType(UserType.Regular);

        service.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        String message = ex.getMessage().replaceAll("\r\n", "");
        return ResponseEntity.badRequest().body(message);
    }

}
