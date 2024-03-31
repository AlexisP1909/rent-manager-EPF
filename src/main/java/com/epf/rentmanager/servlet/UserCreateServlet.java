package com.epf.rentmanager.servlet;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/users/create")
public class UserCreateServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(req, resp);
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
             ClientService.getInstance().create(new Client(
                     1,
                     req.getParameter("last_name"),
                     req.getParameter("first_name"),
                     req.getParameter("email"),
                     LocalDate.parse(req.getParameter("birthdate"), DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        } catch(Exception ex){
            throw new ServletException(ex);
        }
        resp.sendRedirect(req.getContextPath() + "/users");
    }
}
