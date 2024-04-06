package com.epf.rentmanager.service;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exceptions.DaoException;
import com.epf.rentmanager.exceptions.ServiceException;
import com.epf.rentmanager.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class TestServiceClient {
    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientDao clientDao;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void findById_should_fail_when_dao_throws_exception() throws DaoException {
        // When
        when(this.clientDao.findById(1)).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> clientService.findById(1));
    }
    @Test
    void create_should_fail_when_client_has_no_name() {
        // Given
        Client client = new Client(1, "", "John", "johndoe@example.com", LocalDate.now().minusYears(19));

        // Then
        assertThrows(ServiceException.class, () -> clientService.create(client));
    }
    @Test
    void countResaByClient_should_fail_when_dao_throws_exception() throws DaoException {
        // When
        when(this.clientDao.count()).thenThrow(DaoException.class);

        // Then
        assertThrows(ServiceException.class, () -> clientService.countClients());
    }
}
