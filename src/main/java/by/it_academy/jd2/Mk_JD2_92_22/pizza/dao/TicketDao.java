package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.ITicket;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.DaoException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.ITicketDao;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper.TicketMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDao implements ITicketDao {

    private final static String INSERT_SQL = "INSERT INTO structure.ticket(\n" +
            "\tdt_create, dt_update, \"number\", order_id)\n" +
            "\tVALUES (?, ?, ?, ?);";

    private final static String SELECT_BY_ID_SQL = "SELECT tc.id tc_id,\n" +
            "\ttc.dt_create tc_dt_create, \n" +
            "\ttc.dt_update tc_dt_update, \n" +
            "\t\"number\", \n" +
            "\ttc.order_id tc_o_id,\n" +
            "\to.id o_id,\n" +
            "\to.dt_create o_dt_create,\n" +
            "\to.dt_update o_dt_update \n" +
            "\tFROM structure.ticket tc\n" +
            "\tINNER JOIN structure.order_t o ON o.id = tc.order_id" +
            "\tWHERE tc.id = ?;";

    private final static String SELECT_SQL = "SELECT tc.id tc_id,\n" +
            "\ttc.dt_create tc_dt_create, \n" +
            "\ttc.dt_update tc_dt_update, \n" +
            "\tnumber, \n" +
            "\ttc.order_id tc_o_id,\n" +
            "\to.id o_id,\n" +
            "\to.dt_create o_dt_create,\n" +
            "\to.dt_update o_dt_update \n" +
            "\tFROM structure.ticket tc\n" +
            "\tINNER JOIN structure.order_t o ON o.id = tc.order_id;";

    private static final String DELETE_SQL = "DELETE FROM structure.ticket\n" +
            "\tWHERE id = ? and dt_update = ?;";

    private static final String ID = "id";

    private final DataSource ds;

    public TicketDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public ITicket create(ITicket item) throws DaoException {

        ITicket ticket;
        try {
            try (Connection con = ds.getConnection();
                 PreparedStatement stm = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

                stm.setObject(1, item.getDtCreate());
                stm.setObject(2, item.getDtUpdate());
                stm.setString(3, item.getNumber());
                stm.setLong(4, item.getOrder().getId());

                int updated = stm.executeUpdate();

                ResultSet rs = stm.getGeneratedKeys();
                rs.next();
                ticket = read(rs.getLong(ID));

            } catch (SQLException e) {
                throw new RuntimeException("При сохранении данных произошла ошибка", e);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return ticket;
    }

    @Override
    public ITicket read(long id) {

        ITicket ticket;

        try (Connection con = ds.getConnection();
             PreparedStatement stm = con.prepareStatement(SELECT_BY_ID_SQL)) {

            stm.setObject(1, id);

            try (ResultSet rs = stm.executeQuery()) {
                rs.next();
                ticket = TicketMapper.mapper(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }
        return ticket;
    }

    @Override
    public List<ITicket> get() {

        List<ITicket> tickets = new ArrayList<>();

        try (Connection con = ds.getConnection();
             PreparedStatement stm = con.prepareStatement(SELECT_SQL)) {

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    tickets.add(TicketMapper.mapper(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("При чтении данных произошла ошибка", e);
        }
        return tickets;
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
