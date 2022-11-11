package by.it_academy.jd2.Mk_JD2_92_22.pizza.service;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.*;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.ISelectedItemService;

public class SelectedItemServiceSingleton {

    private ISelectedItemService selectedItemService;
    private volatile static SelectedItemServiceSingleton instance;

    public SelectedItemServiceSingleton() {
        //try {
        this.selectedItemService = new SelectedItemService(
                SelectedItemDaoSingleton.getInstance(),
                MenuRowDaoSingleton.getInstance(),
                OrderDaoSingleton.getInstance(),
                TicketDaoSingleton.getInstance());
        // } catch (Exception e) {
        // throw new RuntimeException("Возникли проблемы с созданием слоя доступа к данным", e);
        // }
    }

    public static ISelectedItemService getInstance() {
        if (instance == null) {
            synchronized (SelectedItemServiceSingleton.class) {
                if (instance == null) {
                    instance = new SelectedItemServiceSingleton();
                }
            }
        }
        return instance.selectedItemService;
    }
}
