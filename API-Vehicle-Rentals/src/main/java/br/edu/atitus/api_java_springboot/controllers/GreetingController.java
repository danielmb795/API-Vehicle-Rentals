package br.edu.atitus.api_java_springboot.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ws/greeting")
public class GreetingController {

    @PostMapping
    public ResponseEntity<String> postGreeting(@RequestBody String value) throws Exception {
        if(value.length()>10) {
            throw new Exception("Tamanho do value deve ser no máximo 10");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(value);
    }

    @GetMapping(value={"","/","/{namePath}"})
    public String sayHello(@RequestParam(required = false) String name,
                           @PathVariable(required = false) String namePath) {

        if (name == null) {
            name = namePath!=null ? namePath : "World";
        }

        String returnGreeting = String.format("%s %s!", "Hello", name);
        return returnGreeting;
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        String message = ex.getMessage().replaceAll("\r\n", "");
        return ResponseEntity.badRequest().body(message);
    }

}
