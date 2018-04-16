package booker.model;

import booker.model.id.OrderID;
import booker.model.id.ProgramID;
import booker.util.enums.state.OrderState;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GenericGenerator(name = "myGenerator", strategy = "assigned")
    @GeneratedValue(generator = "myGenerator")
    private OrderID orderID;

    @Column(columnDefinition = "bit default 1", nullable = false)
    private boolean isAutoTicket;

    @Column(nullable = false)
    private double total_price;

    @Column(name = "state", nullable = false)
    private OrderState orderState;

    private String detail;

    private ProgramID programID;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    public OrderID getOrderID() {
        return orderID;
    }

    public void setOrderID(OrderID orderID) {
        this.orderID = orderID;
    }

    public boolean isAutoTicket() {
        return isAutoTicket;
    }

    public void setAutoTicket(boolean autoTicket) {
        isAutoTicket = autoTicket;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public ProgramID getProgramID() {
        return programID;
    }

    public void setProgramID(ProgramID programID) {
        this.programID = programID;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
