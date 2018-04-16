package booker.util.infoCarrier;

import booker.model.Program;
import booker.model.id.ProgramID;
import booker.util.enums.state.ProgramState;
import com.sun.istack.internal.NotNull;

import java.time.LocalDateTime;

public class ProgramShowInfo {

    private ProgramID programID;

    private LocalDateTime start_time;

    private LocalDateTime end_time;

    private String programType;

    private String name;

    private String introduction;

    private ProgramState programState;

    private String caterer;

    private String venueName;

    private String venueAddress;

    private double venuePrice;

    public ProgramShowInfo(Program program) {
        programID = program.getProgramID();
        start_time = program.getStart_time();
        end_time = program.getEnd_time();
        programType = program.getProgramType();
        name = program.getName();
        introduction = program.getIntroduction();
        programState = program.getProgramState();
        caterer = program.getCaterer();
        venueAddress = program.getVenue().getAddress();
        venueName = program.getVenue().getName();
        venuePrice = program.getVenue().getPrice();
    }

    public ProgramID getProgramID() {
        return programID;
    }

    public void setProgramID(ProgramID programID) {
        this.programID = programID;
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public ProgramState getProgramState() {
        return programState;
    }

    public void setProgramState(ProgramState programState) {
        this.programState = programState;
    }

    public String getCaterer() {
        return caterer;
    }

    public void setCaterer(String caterer) {
        this.caterer = caterer;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public double getVenuePrice() {
        return venuePrice;
    }

    public void setVenuePrice(double venuePrice) {
        this.venuePrice = venuePrice;
    }
}
