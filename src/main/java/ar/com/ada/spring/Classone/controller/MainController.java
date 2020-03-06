package ar.com.ada.spring.Classone.controller;

import ar.com.ada.spring.Classone.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity createUser(@RequestBody User user) throws URISyntaxException {
        long count = users.stream().count();

        User userFromList = (count > 0) ?
                users.stream().skip(count - 1).findFirst().orElse(null) :
                null;

        long id = (userFromList != null) ?
                userFromList.getId() + 1 :
                1;

        user.setId(id);
        users.add(user);

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
            userFromList.setLastNane(user.getLastNane());
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
