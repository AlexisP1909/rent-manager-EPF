package com.epf.rentmanager.servlet;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exceptions.DaoException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.sun.net.httpserver.HttpsServer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/users/details")
public class UserDetailsServlet extends HttpServlet{
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            long ClientId = Long.parseLong(req.getParameter("id"));
            List<Reservation> arg = ReservationDao.getInstance().findResaByClientId(ClientId);
            List<Object[]> listeResa = new ArrayList<>();
            List<Vehicle> VehicList= new ArrayList<>();
            for (Reservation res : arg) {
                var vehicle = VehicleDao.getInstance().findById(res.vehicle_id());
                if (!VehicList.contains(vehicle)) {
                    VehicList.add(vehicle);
                    System.out.println(VehicList);
                }
                listeResa.add(new Object[]{res , vehicle});
            }
            req.setAttribute("reservations", listeResa);
            req.setAttribute("voitures", VehicList);
            req.setAttribute("client", ClientDao.getInstance().findById(ClientId));
            req.setAttribute("nbResa", listeResa.size());
            req.setAttribute("nbVoit", VehicList.size());
            req.setAttribute("nbResa", listeResa.size());
        } catch (DaoException e) {
            throw new ServletException(e);

        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/details.jsp").forward(req, resp);
    }
}
