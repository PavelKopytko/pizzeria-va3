package by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.api;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.MenuRowDao;

public class DaoFactory implements IDaoFactory{

    private static final IDaoFactory instance = new DaoFactory();
    private IMenuRowDao menuRowDao;


    private DaoFactory() {
        this.menuRowDao = new MenuRowDao();
    }

    public IMenuRowDao getMenuRowDao(){
        return this.menuRowDao;
    }

    public static IDaoFactory getInstance(){
        return instance;
    }
}
