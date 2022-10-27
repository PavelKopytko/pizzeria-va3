package by.it_academy.jd2.Mk_JD2_92_22.pizza.api;

import java.time.LocalDateTime;

/**
 * Квиток выдаваемый к заказу
 */
public interface ITicket {

    /**
     * Уникальный номер заказа
     * @return
     */
    String getNumber();

    /**
     * Когда заказ получен
     * @return
     */
    LocalDateTime getCreatAt();

    /**
     * Заказ для которого выдали квиток
     * @return
     */
    IOrder getOrder();
}
