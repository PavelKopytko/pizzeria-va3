package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.ITicket;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.exception.DaoException;

import java.time.LocalDateTime;
import java.util.List;

public interface ITicketDao {

    ITicket create(ITicket item) throws DaoException;

    ITicket read(long id) throws DaoException;

    List<ITicket> get() throws DaoException;

    void delete(long id, LocalDateTime dtUpdate) throws DaoException;

    boolean isExist(long id) throws DaoException;
}
