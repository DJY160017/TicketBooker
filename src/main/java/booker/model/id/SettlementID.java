package booker.model.id;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
public class SettlementID implements Serializable {

    @Column(name = "v_account")
    private String venueAccount;

    @Column(name = "s_account")
    private String storeAccount;

    private LocalDateTime settlement_time;

    public String getVenueAccount() {
        return venueAccount;
    }

    public void setVenueAccount(String venueAccount) {
        this.venueAccount = venueAccount;
    }

    public String getStoreAccount() {
        return storeAccount;
    }

    public void setStoreAccount(String storeAccount) {
        this.storeAccount = storeAccount;
    }

    public LocalDateTime getSettlement_time() {
        return settlement_time;
    }

    public void setSettlement_time(LocalDateTime settlement_time) {
        this.settlement_time = settlement_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SettlementID that = (SettlementID) o;

        if (venueAccount != null ? !venueAccount.equals(that.venueAccount) : that.venueAccount != null) return false;
        if (storeAccount != null ? !storeAccount.equals(that.storeAccount) : that.storeAccount != null) return false;
        return settlement_time != null ? settlement_time.equals(that.settlement_time) : that.settlement_time == null;
    }

    @Override
    public int hashCode() {
        int result = venueAccount != null ? venueAccount.hashCode() : 0;
        result = 31 * result + (storeAccount != null ? storeAccount.hashCode() : 0);
        result = 31 * result + (settlement_time != null ? settlement_time.hashCode() : 0);
        return result;
    }
}
