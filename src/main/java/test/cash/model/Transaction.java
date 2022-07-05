package test.cash.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double sum;
    private String phone;
    private String code;
    private Boolean status;

    @OneToOne
    private Currency currency;

    @OneToOne
    private User cashier;

    @OneToOne
    private CashBox cashBox;


    @Override
    public String toString() {
        return "{\"_class\":\"Transaction\", " +
                "\"id\":\"" + id + "\"" + ", " +
                "\"sum\":\"" + sum + "\"" + ", " +
                "\"phone\":" + (phone == null ? "null" : "\"" + phone + "\"") + ", " +
                "\"code\":" + (code == null ? "null" : "\"" + code + "\"") + ", " +
                "\"status\":" + (status == null ? "null" : "\"" + status + "\"") + ", " +
                "\"currency\":" + (currency == null ? "null" : currency) +
                "}";
    }
}
