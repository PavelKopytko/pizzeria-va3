package by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.MenuRowDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.MenuRow;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenuRow;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IPizzaInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MenuRowMapper {

    private final PizzaInfoMapper pizzaInfoMapper;

    public MenuRowMapper(PizzaInfoMapper pizzaInfoMapper) {
        this.pizzaInfoMapper = pizzaInfoMapper;
    }

    public MenuRowDto mapperDto(IMenuRow item) {
        return new MenuRowDto(
                item.getId(),
                item.getDtUpdate().toEpochSecond(ZoneOffset.UTC),
                item.getInfo().getId(),
                pizzaInfoMapper.mapperDto(item.getInfo()),
                item.getPrice()
        );
    }

    public static IMenuRow mapper(ResultSet rs) throws SQLException {

        return new MenuRow(
                rs.getLong("menu_row_id"),
                rs.getObject("mr_dt_create", LocalDateTime.class),
                rs.getObject("mr_dt_update", LocalDateTime.class),
                PizzaInfoFromResultSetMapper.mapper(rs),
                rs.getDouble("price"),
                MenuFromResultSetMapper.mapper(rs)
        );
    }

//    public static MenuRowDto mapperDto(IMenuRow item) {
//        return new MenuRowDto(
//                item.getId(),
//                item.getDtUpdate().toEpochSecond(ZoneOffset.UTC),
//                item.getInfo().getId(),
//                item.getPrice(),
//                item.getMenu().getId()
//        );
//    }

    public static IMenuRow mapperDtoToEntity(MenuRowDto item, IPizzaInfo pizzaInfo, IMenu menu) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return new MenuRow(localDateTime, localDateTime, pizzaInfo, item.getPrice(), menu);
    }

}
