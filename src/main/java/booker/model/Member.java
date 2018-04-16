package booker.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "member")
public class Member implements Serializable {

    @Id
    @GenericGenerator(name = "myGenerator", strategy = "assigned")
    @GeneratedValue(generator = "myGenerator")
    private String mail;

    @Column(nullable = false)
    private String password;

    private String username;

    @Column(columnDefinition = "bit default 0", nullable = false)
    private boolean isCancel;

    @Column(columnDefinition = "int default 0", length = 6, nullable = false)
    private int currentMarks;

    @Column(columnDefinition = "int default 0", length = 15, nullable = false)
    private int accumulateMarks;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public int getCurrentMarks() {
        return currentMarks;
    }

    public void setCurrentMarks(int currentMarks) {
        this.currentMarks = currentMarks;
    }

    public int getAccumulateMarks() {
        return accumulateMarks;
    }

    public void setAccumulateMarks(int accumulateMarks) {
        this.accumulateMarks = accumulateMarks;
    }
}
