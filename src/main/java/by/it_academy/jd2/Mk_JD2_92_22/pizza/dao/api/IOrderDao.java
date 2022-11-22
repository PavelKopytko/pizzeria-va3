package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IOrder;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.exception.DaoException;

public interface IOrderDao extends IDao<IOrder> {

    boolean isExist(long id) throws DaoException;
}
