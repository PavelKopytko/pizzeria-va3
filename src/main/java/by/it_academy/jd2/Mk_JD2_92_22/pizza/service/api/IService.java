package by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api;

import java.time.LocalDateTime;
import java.util.List;

public interface IService<DtoType> {

    DtoType create(DtoType item) throws ServiceException;

    DtoType read(long id);

    List<DtoType> get();

    DtoType update(long id, long dtUpdate, DtoType item);

    void delete(long id, long dtUpdate);
}
