package by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper.api;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IMapperRsToEntity<TYPE> {

    TYPE mapperFromResultSet(ResultSet resultSet) throws SQLException;
}
