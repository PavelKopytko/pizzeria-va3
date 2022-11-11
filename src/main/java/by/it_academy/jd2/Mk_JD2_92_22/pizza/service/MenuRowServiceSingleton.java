package by.it_academy.jd2.Mk_JD2_92_22.pizza.service;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.MenuRowDaoSingleton;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.IMenuRowService;

public class MenuRowServiceSingleton {

    private IMenuRowService menuRowService;
    private volatile static MenuRowServiceSingleton instance;

    public MenuRowServiceSingleton() {
        //try {
        this.menuRowService = new MenuRowService(MenuRowDaoSingleton.getInstance());
        // } catch (Exception e) {
        // throw new RuntimeException("Возникли проблемы с созданием слоя доступа к данным", e);
        // }
    }

    public static IMenuRowService getInstance() {
        if (instance == null) {
            synchronized (MenuRowServiceSingleton.class) {
                if (instance == null) {
                    instance = new MenuRowServiceSingleton();
                }
            }
        }
        return instance.menuRowService;
    }
}
