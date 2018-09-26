/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.htlgrieskirchen.pos3.order.model;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Torsten Welsch
 */
public class Model {

    private List<Order> orders;

    public Model()
    {
        orders = new ArrayList<>();
    }

    public List<Order> getOrders()
    {
        return orders;
    }

    public void addOrder(Order order)
    {
        orders.add(order);
    }

    public void readXml(File file) throws FileNotFoundException
    {
        XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
        orders = (List<Order>) decoder.readObject();
    }

    public void writeXml(File file) throws FileNotFoundException
    {
        final XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
        encoder.writeObject(orders);
    }
}
