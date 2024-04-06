package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exceptions.DaoException;
import com.epf.rentmanager.exceptions.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

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
    @Autowired
    ReservationService reservationService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    ClientService clientService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("vehicles", vehicleService.findAll());
            req.setAttribute("clients", clientService.findAll());
        } catch (DaoException | ServiceException e) {
            throw new ServletException(e);
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println(req.getParameter("client") + " " + req.getParameter("car") + " " + req.getParameter("begin") + " " + req.getParameter("end"));
            reservationService.create(new Reservation(
                    1,
                    Integer.parseInt(req.getParameter("client")),
                    Integer.parseInt(req.getParameter("car")),
                    LocalDate.parse(req.getParameter("begin"), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    LocalDate.parse(req.getParameter("end"), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            ));
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
        resp.sendRedirect(req.getContextPath() + "/rents");

    }
}