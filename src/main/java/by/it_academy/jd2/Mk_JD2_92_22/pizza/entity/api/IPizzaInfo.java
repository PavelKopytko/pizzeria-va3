package by.it_academy.jd2.Mk_JD2_92_22.pizza.entity.api;

/**
 * Информация о пицце
 */
public interface IPizzaInfo {

    /**
     * Название пиццы
     * @return
     */
    String getName();

    /**
     * Описание/состав пиццы
     * @return
     */
    String getDescription();

    /**
     * Итоговый размер пиццы которую приготовят
     * @return
     */
    long getSize();

    long getId();

    void setId(long id);

    void setName(String name);

    void setDescription(String description);

    void setSize(long size);




}
