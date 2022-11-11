package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenuRow;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.DaoException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IMenuRowDao;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper.MenuRowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MenuRowDao implements IMenuRowDao {

    private final static String INSERT_SQL = "INSERT INTO structure.menu_row(\n" +
            "\tdt_create, dt_update, info, price, menu)\n" +
            "\tVALUES ( ?, ?, ?, ?, ?);";

    private final static String SELECT_BY_ID_SQL = "SELECT mr.id menu_row_id, \n" +
            "\tmr.dt_create mr_dt_create, \n" +
            "\tmr.dt_update mr_dt_update, \n" +
            "\tpi.id pi_id, \n" +
            "\tpi.dt_create pi_dt_create, \n" +
            "\tpi.dt_update pi_dt_update, \n" +
            "\tpi.name pi_name, \n" +
            "\tpi.description pi_descr, \n" +
            "\tpi.size pi_size, \n" +
            "\tprice, \n" +
            "\tmenu.id m_id,\n" +
            "\tmenu.name m_name,\n" +
            "\tmenu.dt_create m_dt_create, \n" +
            "\tmenu.dt_update m_dt_update, \n" +
            "\tmenu.enable m_enable\n" +
            "\tFROM structure.menu_row mr\n" +
            "\tINNER JOIN structure.pizza_info pi ON mr.info =pi.id\n" +
            "\tINNER JOIN structure.menu menu ON mr.menu=menu.id\n" +
            "\tWHERE mr.id=?;";

    private final static String SELECT_SQL = "SELECT mr.id menu_row_id, \n" +
            "\tmr.dt_create mr_dt_create, \n" +
            "\tmr.dt_update mr_dt_update, \n" +
            "\tpi.id pi_id, \n" +
            "\tpi.dt_create pi_dt_create, \n" +
            "\tpi.dt_update pi_dt_update, \n" +
            "\tpi.name pi_name, \n" +
            "\tpi.description pi_descr, \n" +
            "\tpi.size pi_size, \n" +
            "\tprice, \n" +
            "\tmenu.id m_id,\n" +
            "\tmenu.name m_name,\n" +
            "\tmenu.dt_create m_dt_create, \n" +
            "\tmenu.dt_update m_dt_update, \n" +
            "\tmenu.enable m_enable\n" +
            "\tFROM structure.menu_row mr\n" +
            "\tINNER JOIN structure.pizza_info pi ON mr.info =pi.id\n" +
            "\tINNER JOIN structure.menu menu ON mr.menu=menu.id;";

    private static final String UPDATE_SQL = "UPDATE structure.menu_row\n" +
            "\tSET dt_update=?, info=?, price=?, menu=?\n" +
            "\tWHERE id =? and dt_update = ?;";

    private static final String DELETE_SQL = "DELETE FROM structure.menu_row\n" +
            "\tWHERE id = ? and dt_update = ?;";

    private static final String UNIQ_ERROR_CODE = "23505";
    private static final String MENU_ROW_INFO_MENU_UNIQ = "menu_row_info_menu_uniq";

    private final DataSource ds;

    public MenuRowDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public IMenuRow create(IMenuRow item) throws DaoException {

        IMenuRow menuRow = null;
        try {
            try (Connection con = ds.getConnection();
                 PreparedStatement stm = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

                stm.setObject(1, item.getDtCreate());
                stm.setObject(2, item.getDtUpdate());
                stm.setLong(3, item.getInfo().getId());
                stm.setDouble(4, item.getPrice());
                stm.setLong(5, item.getMenu().getId());

                int updated = stm.executeUpdate();

                ResultSet rs = stm.getGeneratedKeys();
                rs.next();
                menuRow = read(rs.getLong("id"));

            } catch (SQLException e) {
                if (UNIQ_ERROR_CODE.equals(e.getSQLState())) {
                    if (e.getMessage().contains(MENU_ROW_INFO_MENU_UNIQ)) {
                        throw new RuntimeException("Ошибка, такая строка меню уже существует");
                    }
                }
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return menuRow;
    }

    @Override
    public IMenuRow read(long id) {

        IMenuRow menuRow;

        try (Connection con = ds.getConnection();
             PreparedStatement stm = con.prepareStatement(SELECT_BY_ID_SQL)) {

            stm.setObject(1, id);

            try (ResultSet rs = stm.executeQuery()) {
                rs.next();
                menuRow = MenuRowMapper.mapper(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }


        return menuRow;
    }

    @Override
    public List<IMenuRow> get() {

        List<IMenuRow> menuRows = new ArrayList<>();

        try (Connection con = ds.getConnection();
             PreparedStatement stm = con.prepareStatement(SELECT_SQL)) {

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    menuRows.add(MenuRowMapper.mapper(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }


        return menuRows;
    }

    @Override
    public IMenuRow update(long id, LocalDateTime dtUpdate, IMenuRow item) {
        //IMenuRow menuRow = null;

        try (Connection con = ds.getConnection();
             PreparedStatement stm = con.prepareStatement(UPDATE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stm.setObject(1, item.getDtUpdate());
            stm.setLong(2, item.getInfo().getId());
            stm.setDouble(3, item.getPrice());
            stm.setLong(4, item.getMenu().getId());

            stm.setLong(5, id);
            stm.setObject(6, dtUpdate);

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
}
