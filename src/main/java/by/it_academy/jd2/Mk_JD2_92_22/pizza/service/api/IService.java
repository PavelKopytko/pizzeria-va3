package by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.exception.IDServiceException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.exception.NotUniqServiceException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.exception.ServiceException;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.exception.ValidateException;

import java.time.LocalDateTime;
import java.util.List;

public interface IService<DtoType> {

    DtoType create(DtoType item) throws ServiceException, ValidateException, NotUniqServiceException;

    DtoType read(long id) throws IDServiceException, ServiceException;

    List<DtoType> get() throws ServiceException;

    DtoType update(long id, LocalDateTime dtUpdate, DtoType item) throws ValidateException, ServiceException, NotUniqServiceException;

    void delete(long id, LocalDateTime dtUpdate) throws ServiceException;
}
