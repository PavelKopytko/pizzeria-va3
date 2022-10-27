package by.it_academy.jd2.Mk_JD2_92_22.pizza.api;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.entity.api.IMenuRow;

import java.util.List;

/**
 * Меню
 */
public interface IMenu {
    /**
     * Доступные к заказу пункты
     * @return пункты которые можно заказать
     */
    List<IMenuRow> getItems();
}
