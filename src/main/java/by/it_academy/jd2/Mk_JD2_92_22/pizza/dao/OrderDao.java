package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IOrder;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.DaoException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IOrderDao;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper.OrderMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDao implements IOrderDao {

    private final static String INSERT_SQL = "INSERT INTO structure.order_t(\n" +
            "\tdt_create, dt_update)\n" +
            "\tVALUES ( ?, ?);";

    private final static String SELECT_BY_ID_SQL = "SELECT id o_id, dt_create o_dt_create, dt_update o_dt_update\n" +
            "\tFROM structure.order_t\n" +
            "\tWHERE id = ?;";

    private final static String SELECT_SQL = "SELECT id o_id, dt_create o_dt_create, dt_update o_dt_update\n" +
            "\tFROM structure.order_t;";

//    private static final String UPDATE_SQL = "UPDATE structure.stage\n" +
//            "\tSET dt_update=?, description=?\n" +
//            "\tWHERE id =? and dt_update = ?;";

    private static final String DELETE_SQL = "DELETE FROM structure.order_t\n" +
            "\tWHERE id = ? and dt_update = ?;";

    private static final String ID = "id";

    private final DataSource ds;

    public OrderDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public IOrder create(IOrder item) throws DaoException {

        IOrder order;
        try {
            try (Connection con = ds.getConnection();
                 PreparedStatement stm = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

                stm.setObject(1, item.getDtCreate());
                stm.setObject(2, item.getDtUpdate());

                int updated = stm.executeUpdate();

                ResultSet rs = stm.getGeneratedKeys();
                rs.next();
                order = read(rs.getLong(ID));

            } catch (SQLException e) {
                throw new RuntimeException("При сохранении данных произошла ошибка", e);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return order;
    }

    @Override
    public IOrder read(long id) {

        IOrder order;

        try (Connection con = ds.getConnection();
             PreparedStatement stm = con.prepareStatement(SELECT_BY_ID_SQL)) {

            stm.setObject(1, id);

            try (ResultSet rs = stm.executeQuery()) {
                rs.next();
                order = OrderMapper.mapper(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }
        return order;
    }

    @Override
    public List<IOrder> get() {

        List<IOrder> orders = new ArrayList<>();

        try (Connection con = ds.getConnection();
             PreparedStatement stm = con.prepareStatement(SELECT_SQL)) {

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    orders.add(OrderMapper.mapper(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }
        return orders;
    }

    @Override
    public IOrder update(long id, LocalDateTime dtUpdate, IOrder item) {
//        IOrder order = null;
//
//        try (Connection con = ds.getConnection();
//             PreparedStatement stm = con.prepareStatement(UPDATE_SQL, Statement.RETURN_GENERATED_KEYS);) {
//
//            stm.setObject(1, item.getDtUpdate());
//            stm.setString(2, item.getDescription());
//
//            stm.setLong(3, id);
//            stm.setObject(4, dtUpdate);
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
//        } catch (SQLException e) {
//            throw new RuntimeException("При сохранении данных произошла ошибка", e);
//        }
        return null;
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
