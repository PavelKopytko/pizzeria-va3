package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenu;

import java.util.List;

public interface IMenuFullDao {

    IMenu read(long id);

    List<IMenu> get();
}
