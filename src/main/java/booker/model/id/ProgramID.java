package booker.model.id;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
public class ProgramID implements Serializable {

    @Column(name = "vid")
    private int venueID;

    private LocalDateTime reserve_time;

    public int getVenueID() {
        return venueID;
    }

    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    public LocalDateTime getReserve_time() {
        return reserve_time;
    }

    public void setReserve_time(LocalDateTime reserve_time) {
        this.reserve_time = reserve_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProgramID programID = (ProgramID) o;

        if (venueID != programID.venueID) return false;
        return reserve_time != null ? reserve_time.equals(programID.reserve_time) : programID.reserve_time == null;
    }

    @Override
    public int hashCode() {
        int result = venueID;
        result = 31 * result + (reserve_time != null ? reserve_time.hashCode() : 0);
        return result;
    }
}
