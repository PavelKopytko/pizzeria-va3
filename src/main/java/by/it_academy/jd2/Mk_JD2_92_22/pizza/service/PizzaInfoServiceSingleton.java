package by.it_academy.jd2.Mk_JD2_92_22.pizza.service;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.dao.PizzaInfoDaoSingleton;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.IPizzaInfoService;

public class PizzaInfoServiceSingleton {

    private IPizzaInfoService pizzaInfoService;
    private volatile static PizzaInfoServiceSingleton instance;

    public PizzaInfoServiceSingleton() {
        //try {
            this.pizzaInfoService = new PizzaInfoService(PizzaInfoDaoSingleton.getInstance());
       // } catch (Exception e) {
           // throw new RuntimeException("Возникли проблемы с созданием слоя доступа к данным", e);
       // }
    }

    public static IPizzaInfoService getInstance() {
        if (instance == null) {
            synchronized (PizzaInfoServiceSingleton.class) {
                if (instance == null) {
                    instance = new PizzaInfoServiceSingleton();
                }
            }
        }
        return instance.pizzaInfoService;
    }
}
