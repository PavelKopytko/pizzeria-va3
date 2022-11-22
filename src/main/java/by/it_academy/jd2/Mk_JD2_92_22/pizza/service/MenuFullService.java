package by.it_academy.jd2.Mk_JD2_92_22.pizza.service;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.MenuDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.MenuRowDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.PizzaInfoDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.Menu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenu;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IMenuRow;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IPizzaInfo;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IMenuFullDao;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.exception.DaoException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.exception.NotUniqDaoException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper.MenuFullMapper;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper.MenuMapper;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper.MenuRowMapper;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.helper.mapper.PizzaInfoMapper;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.IMenuFullService;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.IPizzaInfoService;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.exception.IDServiceException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.exception.NotUniqServiceException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.exception.ServiceException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.exception.ValidateException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


public class MenuFullService implements IMenuFullService {

    private final IMenuFullDao menuDao;
    private final IPizzaInfoService pizzaInfoService;
    private final PizzaInfoMapper pizzaInfoMapper= new PizzaInfoMapper();
//    private final MenuMapper menuMapper;
    private final MenuFullMapper menuFullMapper;


    public MenuFullService(IMenuFullDao menuDao, IPizzaInfoService pizzaInfoService, MenuFullMapper menuFullMapper) {
        this.menuDao = menuDao;
        this.pizzaInfoService = pizzaInfoService;
//        this.menuMapper = menuMapper;
        this.menuFullMapper = menuFullMapper;
    }

    @Override
    public MenuDto create(MenuDto item) throws ServiceException, ValidateException, NotUniqServiceException {
        MenuDto menuDto;

        try {
            validate(item);
            List<IMenuRow> menuRows = new ArrayList<>();
            for(MenuRowDto row : item.getItems()){
                PizzaInfoDto pizzaInfoDto = pizzaInfoService.read(row.getInfoId());
                IPizzaInfo pizzaInfo = pizzaInfoMapper.mapperToEntity(pizzaInfoDto);

//                menuRows.add(pizzaInfoMapper.mapperToEntity());
            }
            IMenu menu = menuDao.create(menuRowMapper.getMenuMapper().mapper(item));

            menuDto = this.menuMapper.mapperDto(menu);

        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new ValidateException(e.getMessage(), e);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NotUniqDaoException e) {
            throw new NotUniqServiceException(e.getMessage(), e);
        } catch (IDServiceException e) {
            throw new RuntimeException(e);
        }

        return menuDto;
    }

    @Override
    public MenuDto read(long id) {


        IMenu menu = null;
        try {
            menu = menuDao.read(id);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
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

        try {
            for (IMenu menu : this.menuDao.get()) {

                MenuDto menuDto = menuFullMapper.mapperDto(menu);

                List<MenuRowDto> menuRowDtos = new ArrayList<>();

                for (IMenuRow menuRow : menu.getItems()) {
                    menuRowDtos.add(menuRowMapper.mapperDto(menuRow));
                }
                menuDto.setItems(menuRowDtos);
                menuDtos.add(menuDto);
            }
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        return menuDtos;
    }

    public void validate(MenuDto item) {
        if (item == null) {
            throw new IllegalStateException("Вы не передали меню");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new IllegalArgumentException("Вы не заполнили название меню");
        }
        if (item.getItems() == null || item.getItems().isEmpty()) {
            throw new IllegalArgumentException("Вы не заполнили строки меню");
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
