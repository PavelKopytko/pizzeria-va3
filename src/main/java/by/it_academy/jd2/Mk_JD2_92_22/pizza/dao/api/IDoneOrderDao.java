package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IDoneOrder;

import java.time.LocalDateTime;
import java.util.List;

public interface IDoneOrderDao {

    IDoneOrder create(IDoneOrder item) throws DaoException;

    IDoneOrder read(long id);

    List<IDoneOrder> get();

    void delete(long id, LocalDateTime dtUpdate);
}
