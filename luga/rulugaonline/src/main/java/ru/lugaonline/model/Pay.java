package ru.lugaonline.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true, length = 11, updatable = false)
    private String tel;

    @Column(name = "pay_date", updatable = false)
    private Date payDate;

    @Column(updatable = false)
    private Integer count;

    public Pay() {
    }

    public Pay(String tel, Date payDate, Integer count) {
        this.tel = tel;
        this.payDate = payDate;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pay pay = (Pay) o;
        return Objects.equals(id, pay.id) &&
                Objects.equals(tel, pay.tel) &&
                Objects.equals(payDate, pay.payDate) &&
                Objects.equals(count, pay.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tel, payDate, count);
    }
}
