package booker.model;

import booker.util.enums.state.VenueState;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "venue")
public class Venue implements Serializable {

    @Id
    @Column(name = "vid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int venueID;

    private String password;

    private String name;

    private String city;

    private String address;

    @Column(length = 4)
    private int col_num;

    @Column(length = 5)
    private int raw_num;

    private double price;

    @Column(name = "state")
    private VenueState venueState;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "venue", fetch = FetchType.EAGER)
    private List<Program> programs;

    public int getVenueID() {
        return venueID;
    }

    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCol_num() {
        return col_num;
    }

    public void setCol_num(int col_num) {
        this.col_num = col_num;
    }

    public int getRaw_num() {
        return raw_num;
    }

    public void setRaw_num(int raw_num) {
        this.raw_num = raw_num;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public VenueState getVenueState() {
        return venueState;
    }

    public void setVenueState(VenueState venueState) {
        this.venueState = venueState;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }
}
