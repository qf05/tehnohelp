package ru.lugaonline.model;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true, length = 11, updatable = false)
    @NonNull
    private String tel;

    @Column(name = "id_vk", nullable = false)
    @NonNull
    private int idVk;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pay", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Column(name= "pay_list")
    private List<Pay> payList;

    @Column(name = "add_count")
    private int addCount;

    public User() {
    }

    public User(String tel, int idVk, List<Pay> payList, int addCount) {
        this.tel = tel;
        this.idVk = idVk;
        this.payList = payList;
        this.addCount = addCount;
    }

    public User(String tel) {
        this.tel = tel;
        this.idVk = 0;
        this.payList = new ArrayList<>();
        this.addCount = 0;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getIdVk() {
        return idVk;
    }

    public void setIdVk(int idVk) {
        this.idVk = idVk;
    }

    public int getAddCount() {
        return addCount;
    }

    public void setAddCount(int addCount) {
        this.addCount = addCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Pay> getPayList() {
        return payList;
    }

    public void setPayList(List<Pay> payList) {
        this.payList = payList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return idVk == user.idVk &&
                addCount == user.addCount &&
                Objects.equals(id, user.id) &&
                Objects.equals(tel, user.tel) &&
                Objects.equals(payList, user.payList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tel, idVk, payList, addCount);
    }
}
