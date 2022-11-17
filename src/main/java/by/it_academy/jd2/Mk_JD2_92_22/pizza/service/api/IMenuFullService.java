package by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.MenuDto;

import java.util.List;

public interface IMenuFullService {

    MenuDto read(long id);

    List<MenuDto> get();
}
