package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.DataSourceCreator2;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IMenuRowDao;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dto.MenuRowDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.entity.MenuRow;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.entity.PizzaInfo;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.entity.api.IMenuRow;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.entity.api.IPizzaInfo;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MenuRowDao implements IMenuRowDao {
    @Override
    public List<IMenuRow> get() {

        DataSource dataSource = null;
        try {
            dataSource = DataSourceCreator2.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }

        List<IMenuRow> menuRows = null;

        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement()) {

            try (ResultSet rs = stm.executeQuery(
                    "SELECT r.id,i.id as p_id, n.name, n.descr, i.size, r.price, r.date_update " +
                            "FROM structure.pizza_name as n " +
                            "inner join structure.pizza_info as i on n.id=i.name_id " +
                            "inner join structure.menu_row as r on i.id=r.info_id;")) {

                menuRows = mapList(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return menuRows;
    }

    @Override
    public IMenuRow get(int id) {

        DataSource dataSource = null;
        try {
            dataSource = DataSourceCreator2.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }

        IMenuRow menuRow = null;

        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement()) {

            try (ResultSet rs = stm.executeQuery(
                    "SELECT r.id,i.id as p_id, n.name,n.descr, i.size, r.price, r.date_update " +
                            "FROM structure.pizza_name as n " +
                            "inner join structure.pizza_info as i on n.id=i.name_id " +
                            "inner join structure.menu_row as r on i.id=r.info_id " +
                            "where r.id =" + id + ";")) {

                while (rs.next()) {
                    menuRow = map(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return menuRow;
    }

    @Override
    public void save(MenuRowDto menuRowDto) {

        int info = menuRowDto.getInfo();
        double price = menuRowDto.getPrice();

        DataSource dataSource = null;
        try {
            dataSource = DataSourceCreator2.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }

        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement()) {

            int executeUpdate = stm.executeUpdate(
                    "INSERT INTO structure.menu_row(info_id, price, date_update) " +
                            "VALUES (" + info + "," + price + ", EXTRACT(EPOCH FROM now()));");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void update(int id, int update, MenuRowDto menuRowDto) {

        IMenuRow menuRow = get(id);

        int info = menuRowDto.getInfo();
        double price = menuRowDto.getPrice();


        if (id == menuRow.getId() & update == menuRow.getUpdate()) {

            DataSource dataSource = null;
            try {
                dataSource = DataSourceCreator2.getInstance();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }

            try (Connection conn = dataSource.getConnection();
                 Statement stm = conn.createStatement()) {

                int executeUpdate = stm.executeUpdate(
                        "UPDATE structure.menu_row " +
                                "SET info_id=" + info + ", price=" + price + ", date_update=EXTRACT(EPOCH FROM now()) " +
                                "WHERE id =" + id + ";");

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void delete(int id, int update) {

        DataSource dataSource = null;
        try {
            dataSource = DataSourceCreator2.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }


        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement()) {

            int executeUpdate = stm.executeUpdate(
                    "DELETE FROM structure.menu_row " +
                            " WHERE id = " + id + " and date_update = " + update + ";");

        } catch (
                SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private IMenuRow map(ResultSet rs) throws SQLException {
        IMenuRow menuRow = new MenuRow();
        menuRow.setId(rs.getLong("id"));

        IPizzaInfo pizzaInfo = new PizzaInfo();
        pizzaInfo.setId(rs.getLong("p_id"));
        pizzaInfo.setName(rs.getString("name"));
        pizzaInfo.setDescription(rs.getString("descr"));
        pizzaInfo.setSize(rs.getLong("size"));
        menuRow.setInfo(pizzaInfo);
        menuRow.setPrice(rs.getDouble("price"));
        menuRow.setUpdate(rs.getLong("date_update"));
        return menuRow;
    }

    public List<IMenuRow> mapList(ResultSet rs) throws SQLException {
        List<IMenuRow> menuRows = new ArrayList<>();
        while (rs.next()) {
            menuRows.add(map(rs));
        }
        return menuRows;
    }
}
