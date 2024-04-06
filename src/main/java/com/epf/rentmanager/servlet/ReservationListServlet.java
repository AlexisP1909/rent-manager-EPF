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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet("/rents")
public class ReservationListServlet extends HttpServlet {
    @Autowired
    ReservationService reservationService;
    @Autowired
    ClientService clientService;
    @Autowired
    VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Reservation> arg = reservationService.findAll();
            List<Object[]> listeResa = new ArrayList<>();
            for (Reservation res : arg) {
                listeResa.add(new Object[]{res, clientService.findById(res.client_id()), vehicleService.findById(res.vehicle_id())});
            }
            req.setAttribute("reservations", listeResa);
        } catch (DaoException | ServiceException e) {
            throw new ServletException(e);
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/list.jsp").forward(req, resp);
    }
}
