package com.anshishagua.simplejson;

import java.time.LocalDate;

import java.util.Arrays;
import java.util.List;

class Person {
    private int id;
    private String name;
    private boolean success;
    private Sex sex;
    private Address[] addresses;
    private LocalDate birthday;
    private List<Person> parents;

    public Person() {

    }

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Address[] getAddresses() {
        return addresses;
    }

    public void setAddresses(Address[] addresses) {
        this.addresses = addresses;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public List<Person> getParents() {
        return parents;
    }

    public void setParents(List<Person> parents) {
        this.parents = parents;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", success=" + success +
                ", sex=" + sex +
                ", addresses=" + Arrays.toString(addresses) +
                ", birthday=" + birthday +
                ", parents=" + null +
                '}';
    }
}
