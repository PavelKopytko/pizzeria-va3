package by.it_academy.jd2.Mk_JD2_92_22.pizza.controllers;


import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.MenuDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.MenuServiceSingleton;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.IMenuService;
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
@WebServlet(name = "MenuServlet", urlPatterns = "/menu")
public class MenuServlet extends HttpServlet {

    private IMenuService menuService;
    private final ObjectMapper mapper;
    private final String CHARSET = "UTF-8";
    private final String CONTENT_TYPE = "application/json";


    public MenuServlet() {
        this.menuService = MenuServiceSingleton.getInstance();
        this.mapper = new ObjectMapper();
    }

    //Read POSITION
    //1) Read list
    //2) Read item (card) need id param
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(CHARSET);
        resp.setContentType(CONTENT_TYPE);

        //mapper.registerModule(new JavaTimeModule());
        //mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String queryString = req.getQueryString();

        PrintWriter writer = resp.getWriter();

        if (queryString == null) {

            List<MenuDto> menus = menuService.get();

            writer.write(this.mapper.writeValueAsString(menus));

        } else {

            String idParam = req.getParameter("id");

            long id;

            try {
                id = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            MenuDto menus = menuService.read(id);

            try {
                writer.write(this.mapper.writeValueAsString(menus));
            } catch (IOException e) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        }
    }


    //CREATE POSITION
    //body json
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE);

        //mapper.registerModule(new JavaTimeModule());
        //mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        MenuDto menuDto = null;

        try {
            menuDto = this.mapper.readValue(req.getInputStream(), MenuDto.class);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        MenuDto menu = null;

        try {
            menu = this.menuService.create(menuDto);
        } catch (ServiceException e) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        PrintWriter writer = resp.getWriter();

        writer.write(this.mapper.writeValueAsString(menu));

        resp.setStatus(HttpServletResponse.SC_CREATED);


    }

    //UPDATE POSITION
    //need param id
    //need param version/date_update - optimistic lock
    //body json
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String idParam = req.getParameter("id");
        String updateParam = req.getParameter("update");

        long id;
        long update;
        try {
            id = Integer.parseInt(idParam);
            update = Integer.parseInt(updateParam);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        MenuDto menuDto = this.mapper.readValue(req.getInputStream(), MenuDto.class);

        menuService.update(id, update, menuDto);

        resp.setStatus(HttpServletResponse.SC_CREATED);

    }

    //DELETE POSITION
    //need param id
    //need param version/date_update - optimistic lock
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String idParam = req.getParameter("id");
        String updateParam = req.getParameter("update");

        long id;
        long update;
        try {
            id = Integer.parseInt(idParam);
            update = Integer.parseInt(updateParam);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        menuService.delete(id, update);
    }
}
