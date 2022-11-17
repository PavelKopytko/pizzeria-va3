package by.it_academy.jd2.Mk_JD2_92_22.pizza.service;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.MenuDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.MenuRowDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.Menu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenuRow;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IMenuFullDao;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper.MenuFullMapper;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper.MenuRowMapper;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.IMenuFullService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


public class MenuFullService implements IMenuFullService {

    private final IMenuFullDao menuDao;
    private final MenuFullMapper menuFullMapper;
    private final MenuRowMapper menuRowMapper;


    public MenuFullService(IMenuFullDao menuDao, MenuFullMapper menuFullMapper, MenuRowMapper menuRowMapper) {
        this.menuDao = menuDao;
        this.menuFullMapper = menuFullMapper;
        this.menuRowMapper = menuRowMapper;
    }

    @Override
    public MenuDto read(long id) {

        if (!menuDao.isExist(id)){
            throw new RuntimeException("Нет такого элемента");
        }

        IMenu menu = menuDao.read(id);
        MenuDto menuDto = menuFullMapper.mapperDto(menu);

        List<MenuRowDto> menuRowDtos = new ArrayList<>();

        for (IMenuRow menuRow : menu.getItems()) {
            menuRowDtos.add(menuRowMapper.mapperDto(menuRow));
        }
        menuDto.setItems(menuRowDtos);

        return menuDto;
    }

    @Override
    public List<MenuDto> get() {
        List<MenuDto> menuDtos = new ArrayList<>();

        for (IMenu menu : this.menuDao.get()) {

            MenuDto menuDto = menuFullMapper.mapperDto(menu);

            List<MenuRowDto> menuRowDtos = new ArrayList<>();

            for (IMenuRow menuRow : menu.getItems()) {
                menuRowDtos.add(menuRowMapper.mapperDto(menuRow));
            }
            menuDto.setItems(menuRowDtos);
            menuDtos.add(menuDto);
        }

        return menuDtos;
    }


    private IMenu mapper(MenuDto menuDto) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return new Menu(localDateTime, localDateTime, menuDto.getName(), menuDto.isEnable());
    }

    private MenuDto mapperDto(IMenu menu) {
        return new MenuDto(
                menu.getId(),
                menu.getDtUpdate().toEpochSecond(ZoneOffset.UTC),
                menu.getName(),
                menu.isEnable()
        );
    }

}
