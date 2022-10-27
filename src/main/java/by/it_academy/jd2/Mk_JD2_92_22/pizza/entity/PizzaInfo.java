package by.it_academy.jd2.Mk_JD2_92_22.pizza.entity;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.entity.api.IPizzaInfo;


public class PizzaInfo implements IPizzaInfo {

    private long id;
    private String name;
    private String description;
    private long size;


    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PizzaInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", size=" + size +
                '}';
    }
}
