package by.it_academy.jd2.Mk_JD2_92_22.pizza.service;


import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.MenuRowDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.PizzaInfoDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.MenuRow;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.PizzaInfo;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenuRow;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IPizzaInfo;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.DaoException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IMenuRowDao;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper.MenuRowMapper;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.IMenuRowService;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.ServiceException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class MenuRowService implements IMenuRowService {

    private IMenuRowDao menuRowDao;

    public MenuRowService(IMenuRowDao menuRowDao) {
        this.menuRowDao = menuRowDao;
    }

    @Override
    public MenuRowDto create(MenuRowDto item) throws ServiceException {
        try {
            validate(item);

            IPizzaInfo pizzaInfo = null;
            IMenu menu = null;

            List<IMenuRow> menuRows = menuRowDao.get();
            for (IMenuRow menuRow : menuRows) {
                if (item.getInfo() == menuRow.getInfo().getId()) {
                    pizzaInfo = menuRow.getInfo();
                }
                if (item.getMenu() == menuRow.getMenu().getId()) {
                    menu = menuRow.getMenu();
                }
            }

            IMenuRow menuRow = menuRowDao.create(MenuRowMapper.mapperDtoToEntity(item, pizzaInfo, menu));

            return MenuRowMapper.mapperDto(menuRow);

        } catch (IllegalStateException | IllegalArgumentException | DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public MenuRowDto read(long id) {

        IMenuRow menuRow = menuRowDao.read(id);

        return MenuRowMapper.mapperDto(menuRow);

    }

    @Override
    public List<MenuRowDto> get() {

        List<MenuRowDto> menuRowDtos = new ArrayList<>();

        for (IMenuRow menuRow : menuRowDao.get()) {
            menuRowDtos.add(MenuRowMapper.mapperDto(menuRow));
        }

        return menuRowDtos;
    }

    @Override
    public MenuRowDto update(long id, long dtUpdate, MenuRowDto item) {
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(dtUpdate, 0, ZoneOffset.UTC);

        IMenuRow readed = menuRowDao.read(id);

        if (readed == null) {
            throw new IllegalArgumentException("Строка меню не найдена");
        }
        if (readed.getDtUpdate().toEpochSecond(ZoneOffset.UTC) != dtUpdate) {
            throw new IllegalArgumentException("К сожалению инфо уже было отредактировано кем-то другим");
        }

        readed.setDtUpdate(LocalDateTime.now());
        readed.setInfo(readed.getInfo());
        readed.setPrice(item.getPrice());
        readed.setMenu(readed.getMenu());

        menuRowDao.update(id, localDateTime, readed);

        return MenuRowMapper.mapperDto(readed);
    }

    @Override
    public void delete(long id, long dtUpdate) {

        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(dtUpdate, 0, ZoneOffset.UTC);

        menuRowDao.delete(id, localDateTime);

    }

    public void validate(MenuRowDto item) {
        if (item == null) {
            throw new IllegalStateException("Вы не передали строку меню");
        }
        if (item.getInfo() <= 0) {
            throw new IllegalArgumentException("Вы не заполнили инфо");
        }
        if (item.getPrice() <= 0) {
            throw new IllegalArgumentException("Вы не заполнили цену");
        }
        if (item.getMenu() <= 0) {
            throw new IllegalArgumentException("Вы не указали меню");
        }
        List<IMenuRow> menuRows = menuRowDao.get();

        int count = 0;

        for (IMenuRow menuRow : menuRows) {
            if (item.getInfo() == menuRow.getInfo().getId()) {
                count++;
            }
        }
        if (count == 0) {
            throw new IllegalArgumentException("Вы указали неверный номер инфы");
        }
    }
}
