package by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.Menu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenuRow;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuFullMapper {

    public static IMenu mapper(ResultSet rs) throws SQLException {
        IMenuRow menuRow = MenuRowMapper.mapper(rs);
        return new Menu(
        );
    }
}
