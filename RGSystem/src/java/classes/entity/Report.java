package classes.entity;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;
import javax.persistence.*;

@Entity
@Table(name = "Report")
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name="id")
    private int id;

    @Column(nullable = false, name="filename")
    private String filename;
    @Column(nullable = false, name="file")
    private Blob file;
    @Column(nullable = false, name="timestamp")
    private Timestamp timestamp;
    @Column(nullable = false, name="ay")
    private String ay;
    @Column(nullable = false, name="revise")
    private boolean revise;
    @Column(nullable = false, name="section")
    private String section;
    
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setRevise(boolean revise){
        this.revise = revise;
    }
    public boolean getRevise(){
        return revise;
    }
    
    public void setSection(String section) {
        this.section = section;
    }

    public String getSection() {
        return section;
    }

    public Blob getFile() {
        return file;
    }

    public void setFile(Blob file) {
        this.file = file;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setAY(String ay) {
        this.ay = ay;
    }

    public String getAY() {
        return ay;
    }
}
