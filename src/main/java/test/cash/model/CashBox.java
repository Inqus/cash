package test.cash.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class CashBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double balance;

    public CashBox(Long id, Double balance) {
        this.id = id;
        this.balance = balance;
    }


    @Override
    public String toString() {
        return "{\"_class\":\"CashBox\", " +
                "\"id\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
                "\"balance\":\"" + balance + "\"" +
                "}";
    }
}
