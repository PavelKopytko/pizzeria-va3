package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.PizzaInfo;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IPizzaInfo;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.DaoException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IPizzaInfoDao;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PizzaInfoDao implements IPizzaInfoDao {

    private final static String INSERT_SQL = "INSERT INTO structure.pizza_info(\n" +
            "\tdt_create, dt_update, name, description, size)\n" +
            "\tVALUES ( ?, ?, ?, ?, ?);";

    private final static String SELECT_BY_ID_SQL = "SELECT id, dt_create, dt_update, name, description, size\n" +
            "\tFROM structure.pizza_info\n" +
            "\tWHERE id =?;";

    private final static String SELECT_SQL = "SELECT id, dt_create, dt_update, name, description, size\n" +
            "\tFROM structure.pizza_info;";

    private static final String UPDATE_SQL = "UPDATE structure.pizza_info\n" +
            "\tSET dt_update=?, name=?, description=?, size=?\n" +
            "\tWHERE id =? and dt_update = ?;";

    private static final String DELETE_SQL = "DELETE FROM structure.pizza_info\n" +
            "\tWHERE id = ? and dt_update = ?;";

    private static final String UNIQ_ERROR_CODE = "23505";
    private static final String PIZZA_INFO_NAME_SIZE_UNIQ = "pizza_info_name_size_uniq";

    private final DataSource ds;

    public PizzaInfoDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public IPizzaInfo create(IPizzaInfo item) throws DaoException {

        IPizzaInfo pizzaInfo = null;
        try {
            try (Connection con = ds.getConnection();
                 PreparedStatement stm = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

                stm.setObject(1, item.getDtCreate());
                stm.setObject(2, item.getDtUpdate());
                stm.setString(3, item.getName());
                stm.setString(4, item.getDescription());
                stm.setLong(5, item.getSize());

                int updated = stm.executeUpdate();

                ResultSet rs = stm.getGeneratedKeys();
                rs.next();
                pizzaInfo = read(rs.getLong("id"));

            } catch (SQLException e) {
                if (UNIQ_ERROR_CODE.equals(e.getSQLState())) {
                    if (e.getMessage().contains(PIZZA_INFO_NAME_SIZE_UNIQ)) {
                        throw new RuntimeException("Ошибка, такое название и размер уже существуют");
                    }
                }
            }
        } catch (Exception e){
            throw new DaoException(e);
        }
        return pizzaInfo;
    }

    @Override
    public IPizzaInfo read(long id) {

        IPizzaInfo pizzaInfo = null;

        try (Connection con = ds.getConnection();
             PreparedStatement stm = con.prepareStatement(SELECT_BY_ID_SQL);) {

            stm.setObject(1, id);

            try (ResultSet rs = stm.executeQuery()) {
                rs.next();
                pizzaInfo = mapper(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }


        return pizzaInfo;
    }

    @Override
    public List<IPizzaInfo> get() {

        List<IPizzaInfo> pizzaInfo = new ArrayList<>();

        try (Connection con = ds.getConnection();
             PreparedStatement stm = con.prepareStatement(SELECT_SQL);) {

            try (ResultSet rs = stm.executeQuery();) {
                while (rs.next()) {
                    pizzaInfo.add(mapper(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }


        return pizzaInfo;
    }

    @Override
    public IPizzaInfo update(long id, LocalDateTime dtUpdate, IPizzaInfo item) {
        IPizzaInfo pizzaInfo = null;

        try (Connection con = ds.getConnection();
             PreparedStatement stm = con.prepareStatement(UPDATE_SQL, Statement.RETURN_GENERATED_KEYS);) {

            stm.setObject(1, item.getDtUpdate());
            stm.setString(2, item.getName());
            stm.setString(3, item.getDescription());
            stm.setLong(4, item.getSize());

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

    private IPizzaInfo mapper(ResultSet rs) throws SQLException {
        return new PizzaInfo(
                rs.getLong("id"),
                rs.getObject("dt_create", LocalDateTime.class),
                rs.getObject("dt_update", LocalDateTime.class),
                rs.getString("name"),
                rs.getString("description"),
                rs.getLong("size")
        );
    }
}
