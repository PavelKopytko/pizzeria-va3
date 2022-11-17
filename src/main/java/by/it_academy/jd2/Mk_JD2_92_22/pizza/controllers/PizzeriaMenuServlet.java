package by.it_academy.jd2.Mk_JD2_92_22.pizza.controllers;

import by.it_academy.jd2.Mk_JD2_92_22.pizza.core.dto.MenuDto;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.MenuFullServiceSingleton;
import by.it_academy.jd2.Mk_JD2_92_22.pizza.service.api.IMenuFullService;
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

@WebServlet(name = "PizzeriaMenuServlet", urlPatterns = "/pizzeria/menu")
public class PizzeriaMenuServlet extends HttpServlet {

    private final static String CHARSET = "UTF-8";
    private final static String CONTENT_TYPE = "application/json";
    private final static String ID = "id";

    private IMenuFullService menuFullService;
    private final ObjectMapper mapper;


    public PizzeriaMenuServlet() {
        this.menuFullService = MenuFullServiceSingleton.getInstance();
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

        if (queryString != null) {
            String idParam = req.getParameter(ID);

            long id;
            try {
                id = Long.parseLong(idParam);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            try {
                MenuDto menuDto = menuFullService.read(id);
                writer.write(this.mapper.writeValueAsString(menuDto));
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } else {
            List<MenuDto> menus = menuFullService.get();
            writer.write(this.mapper.writeValueAsString(menus));
        }
    }
}
