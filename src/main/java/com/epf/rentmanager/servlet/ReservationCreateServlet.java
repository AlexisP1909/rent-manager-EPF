package com.epf.rentmanager.servlet;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exceptions.DaoException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.utils.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/rents/create")
public class ReservationCreateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            req.setAttribute("vehicles", VehicleDao.getInstance().findAll());
            req.setAttribute("clients", ClientDao.getInstance().findAll());
        }catch(DaoException e){
            throw new ServletException(e);
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println(req.getParameter("client") + " " + req.getParameter("car") + " " + req.getParameter("begin") + " " + req.getParameter("end"));
            ReservationService.getInstance().create(new Reservation(
                    1,
                    Integer.parseInt(req.getParameter("client")),
                    Integer.parseInt(req.getParameter("car")),
                    LocalDate.parse(req.getParameter("begin"), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    LocalDate.parse(req.getParameter("end"), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    ));
        } catch(Exception ex){
            throw new ServletException(ex);
        }
        resp.sendRedirect(req.getContextPath() + "/rents");

    }
}