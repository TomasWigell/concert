package com.wigell;

import javax.persistence.*;

@Entity
@Table
public class Person {

    @Id
    @Column(name = "personId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    public Person() {
    }

    public Person(int personId, String name, int age) {
        this.personId = personId;
        this.name = name;
        this.age = age;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
