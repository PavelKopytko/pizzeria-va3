package by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.MenuDto;

import java.util.List;

public interface IMenuService /*extends IService<MenuDto>*/ {

    MenuDto create(MenuDto item) throws ServiceException, ValidateException, NotUniqServiceException;

    MenuDto read(long id) throws ServiceException, IDServiceException;

    List<MenuDto> get() throws ServiceException;

    MenuDto update(long id, long dtUpdate, MenuDto item) throws ServiceException, ValidateException;

    void delete(long id, long dtUpdate) throws ServiceException;
}
