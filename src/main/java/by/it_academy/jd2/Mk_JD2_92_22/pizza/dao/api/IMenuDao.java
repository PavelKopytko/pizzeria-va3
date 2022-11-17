package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenu;

import java.time.LocalDateTime;
import java.util.List;

public interface IMenuDao /*extends IDao<IMenu> */{

    IMenu create(IMenu item) throws DaoException, NotUniqDaoException;

    IMenu read(long id) throws DaoException;

    List<IMenu> get() throws DaoException;

    IMenu update(long id, LocalDateTime dtUpdate, IMenu item) throws DaoException;

    void delete(long id, LocalDateTime dtUpdate) throws DaoException;

    boolean isExist(long id) throws DaoException;
}
