package jn.jn_projekt;

import jn.jn_projekt.dao.UserDao;
import jn.jn_projekt.entity.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class JnProjektApplication {

    @Autowired
    private UserDao dao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(JnProjektApplication.class, args);
    }

    @PostConstruct
    public void init() {
        dao.save(new User("Admin", "Admin", "admin",
                passwordEncoder.encode("admin")));
        dao.save(new User("Jakub", "Nowakowski", "jn",
                passwordEncoder.encode("jn")));
    }
}
