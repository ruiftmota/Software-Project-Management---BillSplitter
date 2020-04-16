package gps1920.g31.request_lib.events_information;

import java.io.Serializable;

public class Expense implements Serializable
{
    private static final long serialVersionUID = 1413121110987654321L;

    private String name;
    private double value;

    public Expense(String name, float value)
    {
        this.name = name;
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public double getValue()
    {
        return value;
    }
}