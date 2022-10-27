package by.it_academy.jd2.Mk_JD2_92_22.pizza.entity.api;

/**
 * Строчка меню
 */
public interface IMenuRow {
    /**
     * Информация о пицце
     * @return
     */
    IPizzaInfo getInfo();

    /**
     * Стоимость пиццы
     * @return
     */
    double getPrice();

    long getId();

    void setId(long id);

    void setPrice(double price);

    void setInfo(IPizzaInfo info);

    long getUpdate();

    void setUpdate(long update);
}
