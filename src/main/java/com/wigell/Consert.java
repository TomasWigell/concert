package com.wigell;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table
public class Consert {
    @Id
    @Column(name = "concertId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int concertId;

    @Column(name = "artist")
    private String artist;

    @Column(name = "date")
    private Date date;

    public Consert() {
    }

    public Consert(int concertId, String artist, Date date) {
        this.concertId = concertId;
        this.artist = artist;
        this.date = date;
    }

    public int getConcertId() {
        return concertId;
    }

    public void setConcertId(int concertId) {
        this.concertId = concertId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
