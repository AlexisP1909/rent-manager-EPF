package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exceptions.DaoException;
import com.epf.rentmanager.exceptions.ServiceException;
import com.epf.rentmanager.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestServiceReservation {
    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationDao reservationDao;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void create_should_fail_when_reservation_last_more_than_7_days() {
        // Given
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(8));

        // Then
        assertThrows(ServiceException.class, () -> reservationService.create(reservation));
    }
    @Test
    void countR_should_fail_when_dao_throws_exception() throws DaoException {
        // When
        when(this.reservationDao.count()).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> reservationService.countR());
    }
}
