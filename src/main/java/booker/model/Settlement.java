package booker.model;

import booker.model.id.ProgramID;
import booker.model.id.SettlementID;
import booker.util.enums.state.SettlementState;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "settlement")
public class Settlement implements Serializable {

    @Id
    @GenericGenerator(name = "myGenerator", strategy = "assigned")
    @GeneratedValue(generator = "myGenerator")
    private SettlementID settlementID;

    @Column(name = "v_price")
    private double venueTotalPrice;

    @Column(name = "s_price")
    private double storeTotalPrice;

    @Column(name = "state")
    private SettlementState settlementState;

    private ProgramID programID;

    public SettlementID getSettlementID() {
        return settlementID;
    }

    public void setSettlementID(SettlementID settlementID) {
        this.settlementID = settlementID;
    }

    public double getVenueTotalPrice() {
        return venueTotalPrice;
    }

    public void setVenueTotalPrice(double venueTotalPrice) {
        this.venueTotalPrice = venueTotalPrice;
    }

    public double getStoreTotalPrice() {
        return storeTotalPrice;
    }

    public void setStoreTotalPrice(double storeTotalPrice) {
        this.storeTotalPrice = storeTotalPrice;
    }

    public SettlementState getSettlementState() {
        return settlementState;
    }

    public void setSettlementState(SettlementState settlementState) {
        this.settlementState = settlementState;
    }

    public ProgramID getProgramID() {
        return programID;
    }

    public void setProgramID(ProgramID programID) {
        this.programID = programID;
    }
}
