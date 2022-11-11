package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.DaoException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IMenuDao;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.Menu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenu;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MenuDao implements IMenuDao {

    private final static String INSERT_SQL = "INSERT INTO structure.menu" +
            "(dt_create, dt_update, name, enable)\n" +
            "\tVALUES (?, ?, ?, ?);";

    private final static String SELECT_BY_ID_SQL = "SELECT id, dt_create, dt_update, name, enable " +
            "FROM structure.menu " +
            "WHERE id=?;";

    private final static String SELECT_SQL = "SELECT id, dt_create, dt_update, name, enable " +
            "FROM structure.menu;";

    private static final String UPDATE_SQL = "UPDATE structure.menu\n" +
            "\tSET dt_update = ?, name = ?, enable = ?\n" +
            "\tWHERE id = ? and dt_update = ?;";

    private static final String DELETE_SQL = "DELETE FROM structure.menu\n" +
            "\tWHERE id = ? and dt_update = ?;";

    private final DataSource ds;

    public MenuDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public IMenu create(IMenu item) throws DaoException {

        IMenu menu = null;
        try {
            try (Connection conn = ds.getConnection();
                 PreparedStatement stm = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)
            ) {
                stm.setObject(1, item.getDtCreate());
                stm.setObject(2, item.getDtUpdate());
                stm.setString(3, item.getName());
                stm.setBoolean(4, item.isEnable());

                int updated = stm.executeUpdate();

                //stm.getGeneratedKeys().next();
                ResultSet rs = stm.getGeneratedKeys();
                rs.next();
                menu = read(rs.getLong(1));
                //return read(rs.getLong(1));
            } catch (SQLException e) {
                throw new RuntimeException("При сохранении данных произошла ошибка", e);
            }
        }catch (Exception e){
            throw new DaoException(e);
        }

        return menu;

    }

    @Override
    public IMenu read(long id) {

        IMenu menu = null;
        try (Connection conn = ds.getConnection();
             PreparedStatement stm = conn.prepareStatement(SELECT_BY_ID_SQL)
        ) {
            stm.setObject(1, id);

            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    menu = mapper(rs);
                }
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
                while (rs.next()) {
                    data.add(mapper(rs));

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }

        return data;
    }

    @Override
    public IMenu update(long id, LocalDateTime dtUpdate, IMenu item) {

        IMenu iMenu = null;

        try (Connection conn = ds.getConnection();
             PreparedStatement stm = conn.prepareStatement(UPDATE_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            stm.setObject(1, item.getDtUpdate());
            stm.setString(2, item.getName());
            stm.setBoolean(3, item.isEnable());


            stm.setLong(4, id);
            stm.setObject(5, dtUpdate);

            int countUpdatedRows = stm.executeUpdate();

            if (countUpdatedRows != 1) {
                if (countUpdatedRows == 0) {
                    throw new IllegalArgumentException("Не смогли обновить какую либо запись");
                } else {
                    throw new IllegalArgumentException("Обновили более одной записи");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("При сохранении данных произошла ошибка", e);
        }
        return read(id);
    }

    @Override
    public void delete(long id, LocalDateTime dtUpdate) {
        try (Connection conn = ds.getConnection();
             PreparedStatement stm = conn.prepareStatement(DELETE_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            stm.setLong(1, id);
            stm.setObject(2, dtUpdate);

            int countUpdatedRows = stm.executeUpdate();

            if (countUpdatedRows != 1) {
                if (countUpdatedRows == 0) {
                    throw new IllegalArgumentException("Не смогли удалить какую либо запись");
                } else {
                    throw new IllegalArgumentException("Удалили более одной записи");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("При удалении данных произошла ошибка", e);
        }
    }

    public IMenu mapper(ResultSet rs) throws SQLException {
        return new Menu(
                rs.getLong(1),
                rs.getObject(2, LocalDateTime.class),
                rs.getObject(3, LocalDateTime.class),
                rs.getString(4),
                rs.getBoolean(5)
        );
    }
}
