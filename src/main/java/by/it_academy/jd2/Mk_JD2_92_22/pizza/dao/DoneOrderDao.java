package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.DoneOrder;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IDoneOrder;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.DaoException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IDoneOrderDao;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper.TicketMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DoneOrderDao implements IDoneOrderDao {

    private final static String INSERT_SQL = "INSERT INTO structure.done_order(\n" +
            "\tdt_create, dt_update, ticKet_id)\n" +
            "\tVALUES ( ?, ?, ?, ?, ?);";

    private final static String SELECT_BY_ID_SQL = "SELECT don.id do_id, \n" +
            "\tdon.dt_create do_dt_create, \n" +
            "\tdon.dt_update do_dt_update, \n" +
            "\tdon.ticket_id do_ticket_id\n" +
            "\tFROM structure.done_order AS don\n" +
            "\tWHERE ID = ?;";

    private final static String SELECT_SQL = "SELECT don.id do_id, \n" +
            "\tdon.dt_create do_dt_create, \n" +
            "\tdon.dt_update do_dt_update, \n" +
            "\tdon.ticket_id do_ticket_id\n" +
            "\tFROM structure.done_order AS don;";

    private static final String DELETE_SQL = "DELETE FROM structure.done_order\n" +
            "\tWHERE id = ? and dt_update = ?;";

    private static final String ID = "id";

    private final DataSource ds;

    public DoneOrderDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public IDoneOrder create(IDoneOrder item) throws DaoException {

        IDoneOrder doneOrder;
        try {
            try (Connection con = ds.getConnection();
                 PreparedStatement stm = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

                stm.setObject(1, item.getDtCreate());
                stm.setObject(2, item.getDtUpdate());
                stm.setLong(3, item.getTicket().getId());

                int updated = stm.executeUpdate();

                ResultSet rs = stm.getGeneratedKeys();
                rs.next();
                doneOrder = read(rs.getLong(ID));

            } catch (SQLException e) {
                throw new RuntimeException("При сохранении данных произошла ошибка", e);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return doneOrder;
    }

    @Override
    public IDoneOrder read(long id) {

        IDoneOrder doneOrder;

        try (Connection con = ds.getConnection();
             PreparedStatement stm = con.prepareStatement(SELECT_BY_ID_SQL)) {

            stm.setObject(1, id);

            try (ResultSet rs = stm.executeQuery()) {
                rs.next();
                doneOrder = mapper(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }
        return doneOrder;
    }

    @Override
    public List<IDoneOrder> get() {

        List<IDoneOrder> items = new ArrayList<>();

        try (Connection con = ds.getConnection();
             PreparedStatement stm = con.prepareStatement(SELECT_SQL)) {

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    items.add(mapper(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }
        return items;
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
    private IDoneOrder mapper(ResultSet rs) throws SQLException {
        return new DoneOrder(
                rs.getLong("don_id"),
                rs.getObject("don_dt_create", LocalDateTime.class),
                rs.getObject("don_dt_update", LocalDateTime.class),
                TicketMapper.mapper(rs)
        );
    }
}
