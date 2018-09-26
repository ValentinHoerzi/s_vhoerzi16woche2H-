/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.htlgrieskirchen.pos3.order.model;

import java.util.Objects;

/**
 *
 * @author Torsten Welsch
 */
public class Position {
    private int amount;
    private String description;
    private double pricePerUnit;

    public Position()
    {
    }

    public Position(int amount, String description, double pricePerUnit)
    {
        this.amount = amount;
        this.description = description;
        this.pricePerUnit = pricePerUnit;
    }

    public double calcTotalPrice(){
        return amount*pricePerUnit;
    }
    
    public int getAmount()
    {
        return amount;
    }

    public String getDescription()
    {
        return description;
    }

    public double getPricePerUnit()
    {
        return pricePerUnit;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setPricePerUnit(double pricePerUnit)
    {
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Position other = (Position) obj;
        if (this.amount != other.amount)
        {
            return false;
        }
        if (Double.doubleToLongBits(this.pricePerUnit) != Double.doubleToLongBits(other.pricePerUnit))
        {
            return false;
        }
        if (!Objects.equals(this.description, other.description))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + this.amount;
        hash = 97 * hash + Objects.hashCode(this.description);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.pricePerUnit) ^ (Double.doubleToLongBits(this.pricePerUnit) >>> 32));
        return hash;
    }
    
    
}
