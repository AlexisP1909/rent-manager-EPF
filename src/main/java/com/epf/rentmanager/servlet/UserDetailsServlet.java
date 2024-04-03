package com.epf.rentmanager.servlet;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exceptions.DaoException;
import com.epf.rentmanager.exceptions.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import com.sun.net.httpserver.HttpsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/users/details")
public class UserDetailsServlet extends HttpServlet {
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

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            long ClientId = Long.parseLong(req.getParameter("id"));
            List<Reservation> arg = reservationService.findByClientId(ClientId);
            List<Object[]> listeResa = new ArrayList<>();
            List<Vehicle> VehicList = new ArrayList<>();
            for (Reservation res : arg) {
                var vehicle = vehicleService.findById(res.vehicle_id());
                if (!VehicList.contains(vehicle)) {
                    VehicList.add(vehicle);
                    System.out.println(VehicList);
                }
                listeResa.add(new Object[]{res, vehicle});
            }
            req.setAttribute("reservations", listeResa);
            req.setAttribute("voitures", VehicList);
            req.setAttribute("client", clientService.findById(ClientId));
            req.setAttribute("nbResa", listeResa.size());
            req.setAttribute("nbVoit", VehicList.size());
            req.setAttribute("nbResa", listeResa.size());
        } catch (DaoException e) {
            throw new ServletException(e);

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/details.jsp").forward(req, resp);
    }
}
