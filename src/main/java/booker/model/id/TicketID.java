package booker.model.id;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
public class TicketID implements Serializable {

    private ProgramID programID;

    @Column(name = "raw", length = 4)
    private int raw_num;

    @Column(name = "col", length = 5)
    private int col_num;

    public ProgramID getProgramID() {
        return programID;
    }

    public void setProgramID(ProgramID programID) {
        this.programID = programID;
    }

    public int getRaw_num() {
        return raw_num;
    }

    public void setRaw_num(int raw_num) {
        this.raw_num = raw_num;
    }

    public int getCol_num() {
        return col_num;
    }

    public void setCol_num(int col_num) {
        this.col_num = col_num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketID ticketID = (TicketID) o;

        if (raw_num != ticketID.raw_num) return false;
        if (col_num != ticketID.col_num) return false;
        return programID != null ? programID.equals(ticketID.programID) : ticketID.programID == null;
    }

    @Override
    public int hashCode() {
        int result = programID != null ? programID.hashCode() : 0;
        result = 31 * result + raw_num;
        result = 31 * result + col_num;
        return result;
    }
}
