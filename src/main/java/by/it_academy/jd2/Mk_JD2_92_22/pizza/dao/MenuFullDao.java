package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.Menu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.DaoException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IMenuDao;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IMenuFullDao;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MenuFullDao implements IMenuFullDao {

    private final static String SELECT_BY_ID_SQL = "SELECT id, dt_create, dt_update, name, enable " +
            "FROM structure.menu " +
            "WHERE id=?;";

    private final static String SELECT_SQL = "SELECT id, dt_create, dt_update, name, enable " +
            "FROM structure.menu;";


    private final DataSource ds;

    public MenuFullDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public IMenu read(long id) {

        IMenu menu = null;
        try (Connection conn = ds.getConnection();
             PreparedStatement stm = conn.prepareStatement(SELECT_BY_ID_SQL)
        ) {
            stm.setObject(1, id);

            try (ResultSet rs = stm.executeQuery()) {

                //while (rs.next()) {
                //    menu = M(rs);
                //}
            }
        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }
        return menu;
    }

    @Override
    public List<IMenu> get() {

        List<IMenu> data = new ArrayList<>();

        try (Connection conn = ds.getConnection();
             PreparedStatement stm = conn.prepareStatement(SELECT_SQL)) {

            try (ResultSet rs = stm.executeQuery()) {

                //while (rs.next()) {
                //    data.add(mapper(rs));
//
                //}
            }
        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }
        return data;
    }


}
