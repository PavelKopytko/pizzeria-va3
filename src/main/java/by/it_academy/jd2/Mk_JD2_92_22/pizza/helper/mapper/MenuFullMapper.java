package by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.MenuDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenu;

import java.time.ZoneOffset;

public class MenuFullMapper {

    public MenuDto mapperDto(IMenu item){
        return new MenuDto(
                item.getId(),
                item.getDtUpdate().toEpochSecond(ZoneOffset.UTC),
                item.getName(),
                item.isEnable()
        );

    }
}
