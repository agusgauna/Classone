package ar.com.ada.spring.Classone.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String lastNane;
    private Integer age;

    public User(Long id, String name, String lastNane, Integer age) {
        this.id = id;
        this.name = name;
        this.lastNane = lastNane;
        this.age = age;
    }

    public User(String name, String lastNane, Integer age) {
        this.name = name;
        this.lastNane = lastNane;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User { " +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "lastNane = " + lastNane + ", " +
                "age = " + age + " }";
    }
}
