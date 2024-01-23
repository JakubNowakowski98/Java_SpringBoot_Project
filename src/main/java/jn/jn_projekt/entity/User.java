package jn.jn_projekt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userid;
    
    @Pattern(regexp = "^[A-ZŁŚĆŻŹĘĄÓ][a-zA-Złśćżźęąó]*$", message = "Imię musi zaczynać się od dużej litery")
    @NotBlank(message = "Imię nie może być puste")
    @Size(min = 2, max = 25, message = "Imię musi mieć od 2 do 25 znaków. Nie może zawierać cyfr, ani znaków specjalnych.")
    private String name;

    @Pattern(regexp = "^[A-ZŁŚĆŻŹĘĄÓ][a-zA-Złśćżźęąó]*(-[A-ZŁŚĆŻŹĘĄÓ][a-zA-Złśćżźęąó]*)?$", message = "Nazwisko musi zaczynać się od dużej litery. W przypadku nazwisk dwuczłonowych użyj znaku - oraz wpisz oba człony wielką literą. Nie może zawierać cyfr, ani znaków specjalnych.")
    @NotBlank(message = "Nazwisko nie może być puste")
    @Size(min = 2, max = 50, message = "Nazwisko musi mieć od 2 do 50 znaków")
    private String surname;

    @NotBlank(message = "Login nie może być pusty")
    @Size(min = 2, max = 20, message = "Login musi mieć od 2 do 20 znaków")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Login może zawierać tylko litery i cyfry")
    private String login;

    @NotBlank(message = "Hasło nie może być puste")
    @Size(min = 6, message = "Hasło musi mieć co najmniej 6 znaków")
    private String password;

    public User() {
    }

    public User(String name, String surname, String login,
            String password) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
    }

    //metody get i set
    public Integer getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
