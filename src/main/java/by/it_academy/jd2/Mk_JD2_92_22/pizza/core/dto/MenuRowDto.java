package by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto;

public class MenuRowDto {
    private long id;
    private long dt_update;
    private long info;
    private double price;
    private long menu;

    public MenuRowDto() {
    }

    public MenuRowDto(long id, long dt_update, long info, double price, long menu) {
        this.id = id;
        this.dt_update = dt_update;
        this.info = info;
        this.price = price;
        this.menu = menu;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDt_update() {
        return dt_update;
    }

    public void setDt_update(long dt_update) {
        this.dt_update = dt_update;
    }

    public long getInfo() {
        return info;
    }

    public void setInfo(long info) {
        this.info = info;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getMenu() {
        return menu;
    }

    public void setMenu(long menu) {
        this.menu = menu;
    }
}
