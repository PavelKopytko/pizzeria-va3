package by.it_academy.jd2.Mk_JD2_92_22.pizza.controllers;


import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.PizzaInfoDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.PizzaInfoServiceSingleton;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.IPizzaInfoService;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

//CRUD controller
//IMenuRow
@WebServlet(name = "PizzaInfoServlet", urlPatterns = "/menu/pizza-info")
public class PizzaInfoServlet extends HttpServlet {

    private IPizzaInfoService pizzaInfoService;
    private final ObjectMapper mapper;
    private final static String CHARSET = "UTF-8";
    private final static String CONTENT_TYPE = "application/json";
    private final static String ID = "id";
    private final static String DT_UPDATE = "update";


    public PizzaInfoServlet() {
        this.pizzaInfoService = PizzaInfoServiceSingleton.getInstance();
        this.mapper = new ObjectMapper();
    }

    //Read POSITION
    //1) Read list
    //2) Read item (card) need id param
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(CHARSET);
        resp.setCharacterEncoding(CHARSET);
        resp.setContentType(CONTENT_TYPE);

        String queryString = req.getQueryString();

        PrintWriter writer = resp.getWriter();

        if (queryString == null) {

            List<PizzaInfoDto> items = pizzaInfoService.get();

            writer.write(this.mapper.writeValueAsString(items));

        } else {

            String idParam = req.getParameter(ID);

            long id;

            try {
                id = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            PizzaInfoDto items = pizzaInfoService.read(id);

            try {
                writer.write(this.mapper.writeValueAsString(items));
            } catch (IOException e) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        }
    }


    //CREATE POSITION
    //body json
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(CHARSET);
        resp.setCharacterEncoding(CHARSET);
        resp.setContentType(CONTENT_TYPE);


        PizzaInfoDto pizzaInfoDto = null;
        try {
            pizzaInfoDto = this.mapper.readValue(req.getInputStream(), PizzaInfoDto.class);
        }catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        PizzaInfoDto pizzaInfoDtoOut = null;

        try {
            pizzaInfoDtoOut = this.pizzaInfoService.create(pizzaInfoDto);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
            //resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            //return;
        }

        PrintWriter writer = resp.getWriter();

        writer.write(this.mapper.writeValueAsString(pizzaInfoDtoOut));

        resp.setStatus(HttpServletResponse.SC_CREATED);


    }

    //UPDATE POSITION
    //need param id
    //need param version/date_update - optimistic lock
    //body json
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(CHARSET);
        resp.setCharacterEncoding(CHARSET);
        resp.setContentType(CONTENT_TYPE);

        String idParam = req.getParameter(ID);
        String updateParam = req.getParameter(DT_UPDATE);

        long id;
        long update;
        try {
            id = Integer.parseInt(idParam);
            update = Integer.parseInt(updateParam);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        PizzaInfoDto itemDto = this.mapper.readValue(req.getInputStream(), PizzaInfoDto.class);

        pizzaInfoService.update(id, update, itemDto);

        resp.setStatus(HttpServletResponse.SC_CREATED);

    }

    //DELETE POSITION
    //need param id
    //need param version/date_update - optimistic lock
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(CHARSET);

        String idParam = req.getParameter(ID);
        String updateParam = req.getParameter(DT_UPDATE);

        long id;
        long update;
        try {
            id = Integer.parseInt(idParam);
            update = Integer.parseInt(updateParam);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        pizzaInfoService.delete(id, update);
    }
}
