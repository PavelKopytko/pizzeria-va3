package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.Menu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.MenuRow;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.PizzaInfo;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenuRow;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IPizzaInfo;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IMenuFullDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MenuFullDao implements IMenuFullDao {

    private final static String SELECT_MENU_ITEM_SQL = "SELECT id, dt_create, dt_update, name, enable\n" +
            "\tFROM structure.menu\n" +
            "\tWHERE enable = true;";

    private final static String SELECT_MENU_ITEM_BY_ID_SQL = "SELECT id, dt_create, dt_update, name, enable\n" +
            "\tFROM structure.menu\n" +
            "\tWHERE id = ? ;";


    private final static String SELECT_SQL = "SELECT mr.id menu_row_id,\n" +
            "\tmr.dt_create mr_dt_create,\n" +
            "\tmr.dt_update mr_dt_update,\n" +
            "\tpi.id pi_id,\n" +
            "\tpi.dt_create pi_dt_create,\n" +
            "\tpi.dt_update pi_dt_update,\n" +
            "\tpi.name pi_name,\n" +
            "\tpi.description pi_descr,\n" +
            "\tpi.size pi_size,\n" +
            "\tprice,\n" +
            "\tmenu.id m_id,\n" +
            "\tmenu.name m_name,\n" +
            "\tmenu.dt_create m_dt_create,\n" +
            "\tmenu.dt_update m_dt_update,\n" +
            "\tmenu.enable m_enable\n" +
            "\tFROM structure.menu_row mr\n" +
            "\tINNER JOIN structure.pizza_info pi ON mr.info =pi.id\n" +
            "\tINNER JOIN structure.menu menu ON mr.menu=menu.id\n" +
            "\tWHERE menu.id = ?;";

    private final static String IS_EXISTS = "SELECT EXISTS(SELECT 1 FROM structure.menu WHERE id = ? );";


    private final DataSource ds;

    public MenuFullDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public IMenu read(long id) {


        IMenu menu = null;


        try (Connection connection = ds.getConnection();
             PreparedStatement stmMenu = connection.prepareStatement(SELECT_MENU_ITEM_BY_ID_SQL);
             PreparedStatement stmMenuRow = connection.prepareStatement(SELECT_SQL);
        ) {
            stmMenu.setLong(1, id);

            try (ResultSet rs = stmMenu.executeQuery()) {

                while (rs.next()) {
                    stmMenuRow.setLong(1, id);
                    try (ResultSet rsItem = stmMenuRow.executeQuery()) {
                        menu = mapper(rs, rsItem);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }
        return menu;
    }

    @Override
    public List<IMenu> get() {

        List<IMenu> items = new ArrayList<>();

        try (Connection connection = ds.getConnection();
             PreparedStatement stmMenu = connection.prepareStatement(SELECT_MENU_ITEM_SQL);
             PreparedStatement stmMenuRow = connection.prepareStatement(SELECT_SQL);
        ) {
            try (ResultSet rs = stmMenu.executeQuery()) {
                while (rs.next()) {
                    stmMenuRow.setLong(1, rs.getLong("id"));
                    try (ResultSet rsItem = stmMenuRow.executeQuery()) {
                        items.add(mapper(rs, rsItem));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }
        return items;
    }

    public boolean isExist(long id) {
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(IS_EXISTS);
        ) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery();) {
                rs.next();
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }
    }


    private IMenu mapper(ResultSet rs, ResultSet rsItem) throws SQLException {
        IMenu menu = new Menu(
                rs.getLong("id"),
                rs.getObject("dt_create", LocalDateTime.class),
                rs.getObject("dt_update", LocalDateTime.class),
                rs.getString("name"),
                rs.getBoolean("enable")
        );

        List<IMenuRow> menuRows = new ArrayList<>();
        while (rsItem.next()) {
            IPizzaInfo pizzaInfo = new PizzaInfo(
                    rsItem.getLong("pi_id"),
                    rsItem.getObject("pi_dt_create", LocalDateTime.class),
                    rsItem.getObject("pi_dt_update", LocalDateTime.class),
                    rsItem.getString("pi_name"),
                    rsItem.getString("pi_descr"),
                    rsItem.getLong("pi_size")
            );

            IMenuRow menuRow = new MenuRow(
                    rsItem.getLong("menu_row_id"),
                    rsItem.getObject("mr_dt_create", LocalDateTime.class),
                    rsItem.getObject("mr_dt_update", LocalDateTime.class),
                    pizzaInfo,
                    rsItem.getDouble("price")
            );
            menuRows.add(menuRow);
        }
        menu.setItems(menuRows);

        return menu;
    }

//    private IMenu menuMapper(ResultSet rs) throws SQLException {
//        return new Menu(
//                rs.getLong("id"),
//                rs.getObject("dt_create", LocalDateTime.class),
//                rs.getObject("dt_update", LocalDateTime.class),
//                rs.getString("name"),
//                rs.getBoolean("enable")
//        );


}

//    private IMenuRow mapper(ResultSet rs) throws SQLException {
//        IPizzaInfo pizzaInfo = new PizzaInfo(
//                rs.getLong("pi_id"),
//                rs.getObject("pi_dt_create", LocalDateTime.class),
//                rs.getObject("pi_dt_update", LocalDateTime.class),
//                rs.getString("pi_name"),
//                rs.getString("pi_descr"),
//                rs.getLong("pi_size")
//        );
//        return new MenuRow(
//                rs.getLong("menu_row_id"),
//                rs.getObject("mr_dt_create", LocalDateTime.class),
//                rs.getObject("mr_dt_update", LocalDateTime.class),
//                pizzaInfo,
//                rs.getDouble("price")
//        );
//    }

