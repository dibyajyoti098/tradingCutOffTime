package com.demo.tradingcutofftimes.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name="trade")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @NotNull(message = "iso can't be null")
    private String iso;

    @NotNull(message = "country can't be null")
    private String country;

    @NotNull(message = "cutOffTimes can't be null")
    private String cutOffTimes;

    @NotNull(message = "Date can't be null")
    private String date;


    public Trade() {}

    public Trade(String iso, String country, String cutOffTimes, String date) {
        this.iso = iso;
        this.country = country;
        this.cutOffTimes = cutOffTimes;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCutOffTimes() {
        return cutOffTimes;
    }

    public void setCutOffTimes(String cutOffTimes) {
        this.cutOffTimes = cutOffTimes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", iso='" + iso + '\'' +
                ", country='" + country + '\'' +
                ", cutOffTimes='" + cutOffTimes + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
