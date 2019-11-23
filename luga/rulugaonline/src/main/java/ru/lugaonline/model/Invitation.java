package ru.lugaonline.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true, length = 11, updatable = false)
    private String tel;

    @Column(name = "id_vk", nullable = false, updatable = false)
    private int idVk;

    @Column(name = "id_vk_add", nullable = false, updatable = false)
    private int idVkAdd;

    @Column(nullable = false, updatable = false)
    private Date date;

    public Invitation() {
    }

    public Invitation(String tel, int idVk, int idVkAdd, Date date) {
        this.tel = tel;
        this.idVk = idVk;
        this.idVkAdd = idVkAdd;
        this.date = date;
    }

    public Invitation(String tel, int idVk, int idVkAdd) {
        this.tel = tel;
        this.idVk = idVk;
        this.idVkAdd = idVkAdd;
        this.date = new Date();
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

    public int getIdVkAdd() {
        return idVkAdd;
    }

    public void setIdVkAdd(int idVkAdd) {
        this.idVkAdd = idVkAdd;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invitation add = (Invitation) o;
        return idVk == add.idVk &&
                idVkAdd == add.idVkAdd &&
                Objects.equals(id, add.id) &&
                Objects.equals(tel, add.tel) &&
                Objects.equals(date, add.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tel, idVk, idVkAdd, date);
    }
}
