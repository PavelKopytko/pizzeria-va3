package by.it_academy.jd2.Mk_JD2_92_22.pizza.service;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.DaoFactory;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IMenuRowDao;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dto.MenuRowDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.entity.api.IMenuRow;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.IMenuRowService;

import java.util.List;

public class MenuRowService implements IMenuRowService {

    private IMenuRowDao menuRowDao;

    public MenuRowService() {
        this.menuRowDao = DaoFactory.getInstance().getMenuRowDao();
    }

    @Override
    public List<IMenuRow> get() {
        return this.menuRowDao.get();
    }

    @Override
    public IMenuRow get(int id) {
        return this.menuRowDao.get(id);
    }

    @Override
    public void save(MenuRowDto menuRowDto) {
        validate(menuRowDto);
        this.menuRowDao.save(menuRowDto);

    }

    @Override
    public void update(int id, int update, MenuRowDto menuRowDto ) {
        validate(menuRowDto);
        this.menuRowDao.update(id, update, menuRowDto);

    }

    @Override
    public void delete(int id, int update) {
        this.menuRowDao.delete(id,update);
    }

    @Override
    public void validate(MenuRowDto menuRowDto) {
        if (menuRowDto == null) {
            throw new IllegalStateException("Не указано меню");
        }
        if (menuRowDto.getInfo() <= 0) {
            throw new IllegalArgumentException("Неверное значение info");
        }
        if (menuRowDto.getPrice()<=0){
            throw new IllegalArgumentException("Неверное значение price");
        }
    }
}
