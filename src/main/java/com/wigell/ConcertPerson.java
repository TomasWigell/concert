package com.wigell;

import javax.persistence.*;

@Entity
@Table(name = "concertPerson")
public class ConcertPerson {

    @Id
    @Column(name = "concertPersonId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int concertPersonId;

    @Column(name = "personId")
    private int personId;

    @Column(name = "concertId")
    private int concertId;

    public ConcertPerson() {
    }

    public ConcertPerson(int concertPersonId, int personId, int concertId) {
        this.concertPersonId = concertPersonId;
        this.personId = personId;
        this.concertId = concertId;
    }

    public int getConcertPersonId() {
        return concertPersonId;
    }

    public void setConcertPersonId(int concertPersonId) {
        this.concertPersonId = concertPersonId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getConcertId() {
        return concertId;
    }

    public void setConcertId(int concertId) {
        this.concertId = concertId;
    }
}
