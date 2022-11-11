package by.it_academy.jd2.Mk_JD2_92_22.pizza.service;


import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.PizzaInfoDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.PizzaInfo;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.entity.api.IPizzaInfo;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.DaoException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api.IPizzaInfoDao;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.IPizzaInfoService;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.ServiceException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class PizzaInfoService implements IPizzaInfoService {

    private IPizzaInfoDao pizzaInfoDao;

    public PizzaInfoService(IPizzaInfoDao pizzaInfoDao) {
        this.pizzaInfoDao = pizzaInfoDao;
    }

    @Override
    public PizzaInfoDto create(PizzaInfoDto item) throws ServiceException {
        try {
            validate(item);

            IPizzaInfo pizzaInfo = pizzaInfoDao.create(mapper(item));

            return mapperDto(pizzaInfo);

        } catch (IllegalStateException | IllegalArgumentException | DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PizzaInfoDto read(long id) {

        IPizzaInfo pizzaInfo = pizzaInfoDao.read(id);

        return mapperDto(pizzaInfo);
    }

    @Override
    public List<PizzaInfoDto> get() {

        List<PizzaInfoDto> pizzaInfo = new ArrayList<>();

        for (IPizzaInfo pizza : pizzaInfoDao.get()) {
            pizzaInfo.add(mapperDto(pizza));
        }

        return pizzaInfo;
    }

    @Override
    public PizzaInfoDto update(long id, long dtUpdate, PizzaInfoDto item) {
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(dtUpdate, 0, ZoneOffset.UTC);

        IPizzaInfo readed = pizzaInfoDao.read(id);

        if (readed == null) {
            throw new IllegalArgumentException("Инфо не найдено");
        }
        if (readed.getDtUpdate().toEpochSecond(ZoneOffset.UTC) != dtUpdate) {
            throw new IllegalArgumentException("К сожалению инфо уже было отредактировано кем-то другим");
        }
        readed.setDtUpdate(LocalDateTime.now());
        readed.setName(item.getName());
        readed.setDescription(item.getDescription());
        readed.setSize(item.getSize());

        pizzaInfoDao.update(id, localDateTime, readed);

        return mapperDto(readed);
    }

    @Override
    public void delete(long id, long dtUpdate) {

        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(dtUpdate, 0, ZoneOffset.UTC);

        pizzaInfoDao.delete(id, localDateTime);

    }

    public void validate(PizzaInfoDto item) {
        if (item == null) {
            throw new IllegalStateException("Вы не передали инфо");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new IllegalArgumentException("Вы не заполнили название пиццы");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new IllegalArgumentException("Вы не заполнили описание аиццы");
        }
        if (item.getSize() <= 0) {
            throw new IllegalArgumentException("Вы не заполнили размер пиццы");
        }
    }

    private IPizzaInfo mapper(PizzaInfoDto item) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return new PizzaInfo(localDateTime, localDateTime, item.getName(), item.getDescription(), item.getSize());
    }

    private PizzaInfoDto mapperDto(IPizzaInfo item) {
        return new PizzaInfoDto(
                item.getId(),
                item.getDtUpdate().toEpochSecond(ZoneOffset.UTC),
                item.getName(),
                item.getDescription(),
                item.getSize()
        );
    }
}
