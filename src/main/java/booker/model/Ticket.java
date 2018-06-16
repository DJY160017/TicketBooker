package booker.model;

import booker.model.id.TicketID;
import booker.util.enums.state.TicketState;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ticket")
public class Ticket implements Serializable {

    @Id
    @GenericGenerator(name = "myGenerator", strategy = "assigned")
    @GeneratedValue(generator = "myGenerator")
    private TicketID ticketID;

    @Column(name = "state", length = 3, nullable = false)
    private TicketState ticketState;

    @Column(nullable = false)
    private double price;

    @Column(name = "isCheck", columnDefinition = "bit default 0", nullable = false)
    private boolean check;

    @Column(name="seat_type")
    private String seatType;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(name = "order_ticket",
            joinColumns = {@JoinColumn(name = "ticket_reserve_time", referencedColumnName = "reserve_time"),
                    @JoinColumn(name = "ticket_vid", referencedColumnName = "vid"),
                    @JoinColumn(name = "ticket_raw_num", referencedColumnName = "raw"),
                    @JoinColumn(name = "ticket_col_num", referencedColumnName = "col")},
            inverseJoinColumns = {@JoinColumn(name = "order_userID", referencedColumnName = "mail"),
                    @JoinColumn(name = "order_orderTime", referencedColumnName = "orderTime")})
    private Order order;

    public TicketID getTicketID() {
        return ticketID;
    }

    public void setTicketID(TicketID ticketID) {
        this.ticketID = ticketID;
    }

    public TicketState getTicketState() {
        return ticketState;
    }

    public void setTicketState(TicketState ticketState) {
        this.ticketState = ticketState;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }
}
