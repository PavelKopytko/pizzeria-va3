package by.it_academy.jd2.Mk_JD2_92_22.pizza.service;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.DaoException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IMenuDao;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.MenuDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.Menu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.IMenuService;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.ServiceException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


public class MenuService implements IMenuService {

    private final IMenuDao menuDao;

    public MenuService(IMenuDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    public MenuDto create(MenuDto menuDto) throws ServiceException {

        IMenu menu;

        try{

        validate(menuDto);

        menu = menuDao.create(mapper(menuDto));}

        catch (Exception e){ ///это неправильно
            throw new ServiceException(e);
        }

        //item.setDtCreate(LocalDateTime.now());
        //item.setDtUpdate(LocalDateTime.now());
        //validate

        return mapperDto(menu);
    }

    @Override
    public MenuDto read(long id) {

        IMenu iMenu = menuDao.read(id);

        return mapperDto(iMenu);
    }

    @Override
    public List<MenuDto> get() {
        List<MenuDto> menuDtos = new ArrayList<>();

        for (IMenu menu : menuDao.get()) {
            menuDtos.add(mapperDto(menu));
        }

        return menuDtos;
    }

    @Override
    public MenuDto update(long id, long dtUpdate, MenuDto item) {

        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(dtUpdate, 0, ZoneOffset.UTC);


        IMenu readed = menuDao.read(id);

        if (readed == null) {
            throw new IllegalArgumentException("Меню не найдено");
        }
        if (readed.getDtUpdate().toEpochSecond(ZoneOffset.UTC) != dtUpdate) {
            throw new IllegalArgumentException("К сожалению меню уже было отредактировано кем-то другим");
        }

        readed.setDtUpdate(LocalDateTime.now());
        readed.setName(item.getName());
        readed.setEnable(item.isEnable());
        menuDao.update(id, localDateTime, readed);
        return null;// переделать + маппер
    }

    @Override
    public void delete(long id, long dtUpdate) {

        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(dtUpdate, 0, ZoneOffset.UTC);

        this.menuDao.delete(id, localDateTime);

    }

    public void validate(MenuDto item) {
        if (item == null) {
            throw new IllegalStateException("Вы не передали меню");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new IllegalArgumentException("Вы не заполнили название меню");
        }
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
