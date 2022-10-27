package by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.MenuRowService;

public class ServiceFactory implements IServiceFactory {

    private static final IServiceFactory instance = new ServiceFactory();
    private IMenuRowService menuRowService;

    private ServiceFactory() {
        this.menuRowService = new MenuRowService();
    }

    @Override
    public IMenuRowService getMenuRowService() {
        return menuRowService;
    }

    public static IServiceFactory getInstance() {
        return instance;
    }
}
