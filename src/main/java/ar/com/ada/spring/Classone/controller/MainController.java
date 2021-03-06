package ar.com.ada.spring.Classone.controller;

import ar.com.ada.spring.Classone.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class MainController {
    private List<User> users = new ArrayList<>();

    @GetMapping({ "", "/" })
    public ResponseEntity getAllUsers() {
        return ResponseEntity.ok().body(users);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity createUser(@Valid @RequestBody User user) throws URISyntaxException {
        //cuanto es la cantidad de elementos que hay en la lista
        long count = users.stream().count();

        //if ternary: evalua si la cantidad en la lista es mayor a cero
        User userFromList = (count > 0) ?
                // en la instruccion true: de la lista saca el ultimo elemento insertado
                // y lo retorna
                users.stream().skip(count - 1).findFirst().orElse(null) :
                // en la instruccion false: retorna un null
                null;

        //if ternary: para definir el nuevo id del usuario a insertar en la lista
        long id = (userFromList != null) ?
                // en instruccion true: extrae el id del ultimo usuario
                // le suma uno y retorna el nuevo valor
                userFromList.getId() + 1 :
                // en la instruccion false retorna uno directamente porque la lista esta vacia
                1;

        // asigno el id generado en el if ternary anterior al usuario que se quiere sacar
        user.setId(id);

        //agrego el usuario nuevo a la lista
        users.add(user);

        //retorno el response de la peticion POST
        return ResponseEntity
                .created(new URI("/users/" + user.getId()))
                .body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@PathVariable Integer id) {
        User user = users.stream()
                .filter(u -> u.getId().intValue() == id)
                .findAny()
                .orElse(null);

        HttpStatus httpStatus = user != null ?
                HttpStatus.OK :
                HttpStatus.NOT_FOUND;

        return ResponseEntity.status(httpStatus).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUserById(@RequestBody User user, @PathVariable Integer id) {
        User userFromList = users.stream()
                .filter(u -> u.getId().intValue() == id)
                .findAny()
                .orElse(null);

        HttpStatus httpStatus = null;

        if (userFromList != null) {
            httpStatus = HttpStatus.OK;
            userFromList.setName(user.getName());
            userFromList.setLastName(user.getLastName());
            userFromList.setAge(user.getAge());
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(httpStatus).body(userFromList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        boolean hasDelete = users.removeIf(u -> u.getId().intValue() == id);

        Map<String, String> map = new HashMap<>();
        map.put("error", HttpStatus.BAD_REQUEST.toString());
        map.put("message", "user id not exist");

        return (hasDelete) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }
}
