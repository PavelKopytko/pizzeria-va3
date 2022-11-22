package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.ISelectedItem;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.exception.DaoException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.ISelectedItemDao;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper.SelectedItemMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SelectedItemDao implements ISelectedItemDao {

    private final static String INSERT_SQL = "INSERT INTO structure.selected_item(\n" +
            "\tdt_create, dt_update, menu_row, count, order_id)\n" +
            "\tVALUES ( ?, ?, ?, ?, ?);";

    private final static String SELECT_BY_ID_SQL = "SELECT si.id si_id, \n" +
            "\tsi.dt_create si_dt_create, \n" +
            "\tsi.dt_update si_dt_update, \n" +
            "\tsi.menu_row si_menu_row, \n" +
            "\tcount, \n" +
            "\tsi.order_id si_order,\n" +
            "\to.id o_id,\n" +
            "\to.dt_create o_dt_create, \n" +
            "\to.dt_update o_dt_update,\n" +
            "\tmr.id menu_row_id,\n" +
            "\tmr.dt_create mr_dt_create, \n" +
            "\tmr.dt_update mr_dt_update, \n" +
            "\tpi.id pi_id,\n" +
            "\tpi.dt_create pi_dt_create,\n" +
            "\tpi.dt_update pi_dt_update,\n" +
            "\tpi.name pi_name, \n" +
            "\tpi.description pi_descr,\n" +
            "\tpi.size pi_size,\n" +
            "\tprice, \n" +
            "\tmenu.id m_id,\n" +
            "\tmenu.name m_name,\n" +
            "\tmenu.dt_create m_dt_create, \n" +
            "\tmenu.dt_update m_dt_update, \n" +
            "\tmenu.enable m_enable\n" +
            "\tFROM structure.selected_item si\n" +
            "\tINNER JOIN structure.menu_row mr ON si.menu_row = mr.id\n" +
            "\tINNER JOIN structure.order_t o ON si.order_id = o.id\n" +
            "\tINNER JOIN structure.pizza_info pi ON mr.info =pi.id\n" +
            "\tINNER JOIN structure.menu menu ON mr.menu=menu.id\n" +
            "\tWHERE si.id = ?;";

    private final static String SELECT_SQL = "SELECT si.id si_id, \n" +
            "\tsi.dt_create si_dt_create, \n" +
            "\tsi.dt_update si_dt_update, \n" +
            "\tsi.menu_row si_menu_row, \n" +
            "\tcount, \n" +
            "\tsi.order_id si_order,\n" +
            "\to.id o_id,\n" +
            "\to.dt_create o_dt_create, \n" +
            "\to.dt_update o_dt_update,\n" +
            "\tmr.id menu_row_id,\n" +
            "\tmr.dt_create mr_dt_create, \n" +
            "\tmr.dt_update mr_dt_update, \n" +
            "\tpi.id pi_id,\n" +
            "\tpi.dt_create pi_dt_create,\n" +
            "\tpi.dt_update pi_dt_update,\n" +
            "\tpi.name pi_name, \n" +
            "\tpi.description pi_descr,\n" +
            "\tpi.size pi_size,\n" +
            "\tprice, \n" +
            "\tmenu.id m_id,\n" +
            "\tmenu.name m_name,\n" +
            "\tmenu.dt_create m_dt_create, \n" +
            "\tmenu.dt_update m_dt_update, \n" +
            "\tmenu.enable m_enable\n" +
            "\tFROM structure.selected_item si\n" +
            "\tINNER JOIN structure.menu_row mr ON si.menu_row = mr.id\n" +
            "\tINNER JOIN structure.order_t o ON si.order_id = o.id\n" +
            "\tINNER JOIN structure.pizza_info pi ON mr.info =pi.id\n" +
            "\tINNER JOIN structure.menu menu ON mr.menu=menu.id";

    private static final String DELETE_SQL = "DELETE FROM structure.selected_item\n" +
            "\tWHERE id = ? and dt_update = ?;";

    //private static final String UNIQ_ERROR_CODE = "23505";
    //private static final String MENU_ROW_INFO_MENU_UNIQ = "menu_row_info_menu_uniq";

    private final DataSource ds;

    public SelectedItemDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public ISelectedItem create(ISelectedItem item) throws DaoException {

        ISelectedItem selectedItem = null;
        try {
            try (Connection con = ds.getConnection();
                 PreparedStatement stm = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

                stm.setObject(1, item.getDtCreate());
                stm.setObject(2, item.getDtUpdate());
                stm.setLong(3, item.getMenuRow().getId());
                stm.setDouble(4, item.getCount());
                stm.setLong(5, item.getOrder().getId());

                int updated = stm.executeUpdate();

                ResultSet rs = stm.getGeneratedKeys();
                rs.next();
                selectedItem = read(rs.getLong("id"));

            } catch (SQLException e) {
                throw new RuntimeException("При сохранении данных произошла ошибка", e);

            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return selectedItem;
    }

    @Override
    public ISelectedItem read(long id) {

        ISelectedItem selectedItem;

        try (Connection con = ds.getConnection();
             PreparedStatement stm = con.prepareStatement(SELECT_BY_ID_SQL)) {

            stm.setObject(1, id);

            try (ResultSet rs = stm.executeQuery()) {
                rs.next();
                selectedItem = SelectedItemMapper.mapper(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }


        return selectedItem;
    }

    @Override
    public List<ISelectedItem> get() {

        List<ISelectedItem> selectedItems = new ArrayList<>();

        try (Connection con = ds.getConnection();
             PreparedStatement stm = con.prepareStatement(SELECT_SQL)) {

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    selectedItems.add(SelectedItemMapper.mapper(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }


        return selectedItems;
    }

    @Override
    public ISelectedItem update(long id, LocalDateTime dtUpdate, ISelectedItem item) {
        //IMenuRow menuRow = null;

//        try (Connection con = ds.getConnection();
//             PreparedStatement stm = con.prepareStatement(UPDATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
//
//            stm.setObject(1, item.getDtUpdate());
//            stm.setLong(2, item.getInfo().getId());
//            stm.setDouble(3, item.getPrice());
//            stm.setLong(4, item.getMenu().getId());
//
//            stm.setLong(5, id);
//            stm.setObject(6, dtUpdate);
//
//            int countUpdatedRows = stm.executeUpdate();
//
//            if (countUpdatedRows != 1) {
//                if (countUpdatedRows == 0) {
//                    throw new IllegalArgumentException("Не смогли обновить какую либо запись");
//                } else {
//                    throw new IllegalArgumentException("Обновили более одной записи");
//                }
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException("При сохранении данных произошла ошибка", e);
//        }

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
