/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.htlgrieskirchen.pos3.order.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Torsten Welsch
 */
public class Order {

    public static long globalId = 1;
    private long id;
    private Date date;
    private String lastName;
    private String firstName;
    private String street;
    private int zipCode;
    private String city;
    private List<Position> positions;

    public Order()
    {
        positions=new ArrayList<>();
    }

    public Order(Date date, String lastName, String firstName, String street, int zipCode, String city)
    {
        this.id = globalId++;
        this.date = date;
        this.lastName = lastName;
        this.firstName = firstName;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
        positions=new ArrayList<>();
    }

    public void addPosition(Position pos)
    {
        positions.add(pos);
    }

    public double calcTotalPrice()
    {
        double erg = 0;
        if(!positions.isEmpty()){
            erg = positions.stream().map((p) -> p.calcTotalPrice()).reduce(erg, (accumulator, _item) -> accumulator + _item);
        }     
        return erg;
    }

    public static long getGlobalid()
    {
        return globalId;
    }

    public long getId()
    {
        return id;
    }

    public Date getDate()
    {
        return date;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getStreet()
    {
        return street;
    }

    public int getZipCode()
    {
        return zipCode;
    }

    public String getCity()
    {
        return city;
    }

    public List<Position> getPositions()
    {
        return positions;
    }

    public static void setGlobalid(long globalid)
    {
        Order.globalId = globalid;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public void setZipCode(int zipCode)
    {
        this.zipCode = zipCode;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setPositions(List<Position> positions)
    {
        this.positions = positions;
    }

    public static long getGlobalId()
    {
        return globalId;
    }

    public static void setGlobalId(long globalId)
    {
        Order.globalId = globalId;
    }
    
    
}