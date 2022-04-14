package com.dkop.library.tests.dao;

import com.dkop.library.dao.UserDao;
import com.dkop.library.dao.impls.UserDaoImpl;
import com.dkop.library.entity.User;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToDeleteException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static com.dkop.library.controller.command.CommandUtils.messagesBundle;
import static com.dkop.library.dao.impls.Queries.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserDaoTest {

    private UserDao userDao;
    private final Connection mockConnection = mock(Connection.class);
    private final PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    private final ResultSet mockResultSet = mock(ResultSet.class);

    @Before
    public void setUp() throws SQLException {
        userDao = new UserDaoImpl(mockConnection);
        messagesBundle = mock(ResourceBundle.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    public void testCreate() throws SQLException {
        userDao.create(createTestUser());
        verify(mockConnection, times(1)).prepareStatement(CREATE_USER);
        verify(mockPreparedStatement, times(1)).setString(1, "Ivan");
        verify(mockPreparedStatement, times(1)).setString(2, "Ivanov");
        verify(mockPreparedStatement, times(1)).setString(3, "ivan@mail.com");
        verify(mockPreparedStatement, times(1)).setString(4, "12345");
        verify(mockPreparedStatement, times(1)).setString(5, "role");
        verify(mockPreparedStatement, times(1)).setString(6, "status");
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindAll() {
        userDao.findAll();
    }

    @Test
    public void testCountAllRowsByRole() throws SQLException {
        int count = userDao.countAllRowsByRole("role");
        verify(mockConnection, times(1)).prepareStatement(COUNT_ROWS_BY_ROLE);
        verify(mockPreparedStatement, times(1)).setString(1, "role");
        verify(mockPreparedStatement, times(1)).executeQuery();
        Assert.assertEquals(0, count);
    }

    @Test
    public void testFindAllByRole() throws SQLException {
        List<User> allUsers = userDao.findAllByRole("role", 1, 5);
        verify(mockConnection, times(1)).prepareStatement(SELECT_USERS_BY_ROLE);
        verify(mockPreparedStatement, times(1)).setString(1, "role");
        verify(mockPreparedStatement, times(1)).setInt(2, 1);
        verify(mockPreparedStatement, times(1)).setInt(3, 5);
        verify(mockPreparedStatement, times(1)).executeQuery();
        Assert.assertTrue(allUsers.isEmpty());
    }

    @Test
    public void testChangeStatus() throws SQLException {
        userDao.changeStatus(1, "changed");
        verify(mockConnection, times(1)).prepareStatement(UPDATE_USER_STATUS);
        verify(mockPreparedStatement, times(1)).setString(1, "changed");
        verify(mockPreparedStatement, times(1)).setInt(2, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test(expected = NotFoundException.class)
    public void testFindById() throws NotFoundException, SQLException {
        userDao.findById(1);
        verify(mockConnection, times(1)).prepareStatement(SELECT_USER_BY_ID);
        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpdate() {
        userDao.update(createTestUser());
    }

    @Test
    public void testDelete() throws UnableToDeleteException, SQLException {
        userDao.delete(1);
        verify(mockConnection, times(1)).prepareStatement(DELETE_USER_BY_ID);
        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test(expected = UnableToDeleteException.class)
    public void testDeleteThrows() throws UnableToDeleteException, SQLException {
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());
        try {
            userDao.delete(1);
        } catch (UnableToDeleteException e) {
            verify(mockConnection, times(1)).prepareStatement(DELETE_USER_BY_ID);
            verify(mockPreparedStatement, times(1)).setInt(1, 1);
            verify(mockPreparedStatement, times(1)).executeUpdate();
            throw e;
        }
    }

    @Test(expected = DoesNotExistException.class)
    public void testFindByEmail() throws SQLException, DoesNotExistException {
        userDao.findByEmail("ivanov@mail.com");
        verify(mockConnection, times(1)).prepareStatement(SELECT_USER_BY_EMAIL);
        verify(mockPreparedStatement, times(1)).setString(1, "ivanov@mail.com");
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    private User createTestUser() {
        return User.newBuilder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivan@mail.com")
                .password("12345")
                .role("role")
                .status("status")
                .id(1)
                .build();

    }
}
