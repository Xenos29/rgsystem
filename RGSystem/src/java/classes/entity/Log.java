package classes.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

@Entity
@Table(name = "Log")
public class Log implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name="id")
    private int id;
    
    @Column(nullable = false, name="action")
    private String action;
    @Column(nullable = false, name="timestamp")
    private Timestamp timestamp;
    @Column(nullable = false, name="section")
    private String section;
   

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setSection(String section) {
        this.section = section;
    }

    public String getSection() {
        return section;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

}
