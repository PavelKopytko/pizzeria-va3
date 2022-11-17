package by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.PizzaInfoDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IPizzaInfo;

import java.time.ZoneOffset;

public class PizzaInfoMapper {

    public PizzaInfoDto mapperDto(IPizzaInfo item) {
        return new PizzaInfoDto(
                item.getId(),
                item.getDtUpdate().toEpochSecond(ZoneOffset.UTC),
                item.getName(),
                item.getDescription(),
                item.getSize()
        );
    }
}
