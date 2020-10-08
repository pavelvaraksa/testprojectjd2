package com.noirix.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

public class User {
    /*Here we will store PK of m_users table*/
    private Long id;

    private String name;
    private String surname;
    private Date birthDate;
    private Gender gender = Gender.NOT_SELECTED;
    private Timestamp created;
    private Timestamp changed;
    private Float weight;

    public User() {
    }

    public User(Long id, String name, String surname, Date birthDate, Gender gender, Timestamp created, Timestamp changed, Float weight) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.created = created;
        this.changed = changed;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getChanged() {
        return changed;
    }

    public void setChanged(Timestamp changed) {
        this.changed = changed;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(birthDate, user.birthDate) &&
                gender == user.gender &&
                Objects.equals(created, user.created) &&
                Objects.equals(changed, user.changed) &&
                Objects.equals(weight, user.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, birthDate, gender, created, changed, weight);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
