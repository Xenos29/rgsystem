package classes.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "User")
public class User implements Serializable {

    @Id
    @Column(nullable = true, name="section")
    private String section;
    
    @Column(nullable = false, name="username")
    private String username;
    @Column(nullable = false, name="password")
    private String password;
    @Column(nullable = false, name="name")
    private String name;
    @Column(nullable = false, name="position")
    private String position;
     
    public void setSection(String section) {
        this.section = section;
    }

    public String getSection() {
        return section;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

}
