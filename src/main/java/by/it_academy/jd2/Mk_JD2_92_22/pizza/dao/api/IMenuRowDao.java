package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.dto.MenuRowDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.entity.api.IMenuRow;

import java.util.List;

public interface IMenuRowDao {

    List<IMenuRow> get();

    IMenuRow get(int id);

    void save(MenuRowDto menuRowDto);

    void update(int id, int update, MenuRowDto menuRowDto);

    void delete(int id, int update);
}
