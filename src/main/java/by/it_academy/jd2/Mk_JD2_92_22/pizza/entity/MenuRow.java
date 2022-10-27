package by.it_academy.jd2.Mk_JD2_92_22.pizza.entity;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.entity.api.IMenuRow;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.entity.api.IPizzaInfo;

public class MenuRow implements IMenuRow {

    private long id;
    private IPizzaInfo info;
    private double price;

    private long update;

    @Override
    public long getId() {
        return id;
    }

    @Override

    public void setId(long id) {
        this.id = id;
    }

    @Override

    public IPizzaInfo getInfo() {
        return info;
    }

    @Override
    public void setInfo(IPizzaInfo info) {
        this.info = info;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public long getUpdate() {
        return update;
    }
    @Override
    public void setUpdate(long update) {
        this.update = update;
    }
}
