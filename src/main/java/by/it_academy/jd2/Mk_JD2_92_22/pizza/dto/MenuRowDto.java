package by.it_academy.jd2.Mk_JD2_92_22.pizza.dto;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.entity.api.IPizzaInfo;

public class MenuRowDto {

    private int info;
    private double price;

    public int getInfo() {
        return info;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
