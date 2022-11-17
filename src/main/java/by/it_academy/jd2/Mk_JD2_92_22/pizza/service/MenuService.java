package by.it_academy.jd2.Mk_JD2_92_22.pizza.service;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.MenuDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.Menu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.DaoException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IMenuDao;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.NotUniqDaoException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.*;

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
    public MenuDto create(MenuDto menuDto) throws ServiceException, ValidateException, NotUniqServiceException {

        IMenu menu = null;

        try {
            validate(menuDto);
            menu = menuDao.create(mapper(menuDto));

        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new ValidateException(e.getMessage(), e);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NotUniqDaoException e) {
            throw new NotUniqServiceException(e.getMessage(), e);
        }

        return mapperDto(menu);
    }

    @Override
    public MenuDto read(long id) throws ServiceException, IDServiceException {
        IMenu iMenu = null;

        try {
            if (menuDao.isExist(id)) {
                iMenu = menuDao.read(id);
            } else {
                throw new IDServiceException("Такого id не существует");
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return mapperDto(iMenu);
    }

    @Override
    public List<MenuDto> get() throws ServiceException {
        List<MenuDto> menuDtos = new ArrayList<>();

        try {
            for (IMenu menu : menuDao.get()) {
                menuDtos.add(mapperDto(menu));
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return menuDtos;
    }

    @Override
    public MenuDto update(long id, long dtUpdate, MenuDto item) throws ServiceException, ValidateException {

        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(dtUpdate, 0, ZoneOffset.UTC);

        try{
            validate(item);
        }catch (IllegalStateException | IllegalArgumentException e) {
            throw new ValidateException(e.getMessage(), e);
        }

        IMenu readed = null;
        try {
            readed = menuDao.read(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        if (readed == null) {
            throw new IllegalArgumentException("Меню не найдено");
        }
        if (readed.getDtUpdate().toEpochSecond(ZoneOffset.UTC) != dtUpdate) {
            throw new IllegalArgumentException("К сожалению меню уже было отредактировано кем-то другим");
        }

        readed.setDtUpdate(LocalDateTime.now());
        readed.setName(item.getName());
        readed.setEnable(item.isEnable());

        try {
            menuDao.update(id, localDateTime, readed);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage(),e);
        }
        return null;// переделать + маппер
    }

    @Override
    public void delete(long id, long dtUpdate) throws ServiceException {

        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(dtUpdate, 0, ZoneOffset.UTC);

        try {
            this.menuDao.delete(id, localDateTime);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(),e);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage(),e);
        }

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
