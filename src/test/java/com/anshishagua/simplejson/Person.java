package com.anshishagua.simplejson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Person {
    private int id;
    private String name;
    private boolean success;
    private List<Person> persons = new ArrayList<>();

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

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Person> getPersons() {
        return persons;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", success=" + success +
                ", persons=" + persons +
                '}';
    }
}
