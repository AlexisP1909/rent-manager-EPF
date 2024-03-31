package com.epf.rentmanager.servlet;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exceptions.DaoException;
import com.epf.rentmanager.model.Reservation;

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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Reservation> arg = ReservationDao.getInstance().findAll();
            List<Object[]> listeResa = new ArrayList<>();
            for (Reservation res : arg) {
                listeResa.add(new Object[]{res , ClientDao.getInstance().findById(res.client_id()), VehicleDao.getInstance().findById(res.vehicle_id())});
            }
            req.setAttribute("reservations", listeResa);
        } catch (DaoException e) {
            throw new ServletException(e);
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/list.jsp").forward(req, resp);
    }
}
