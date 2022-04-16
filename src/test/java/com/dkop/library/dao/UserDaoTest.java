package com.dkop.library.dao;

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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserDaoTest {

    private UserDao userDao;
    private final Connection connectionMock = mock(Connection.class);
    private final PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
    private final ResultSet resultSetMock = mock(ResultSet.class);

    @Before
    public void setUp() throws SQLException {
        userDao = new UserDaoImpl(connectionMock);
        messagesBundle = mock(ResourceBundle.class);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    }

    @Test
    public void testCreate() throws SQLException {
        userDao.create(createTestUser());
        verify(connectionMock, times(1)).prepareStatement(CREATE_USER);
        verify(preparedStatementMock, times(1)).setString(1, "Ivan");
        verify(preparedStatementMock, times(1)).setString(2, "Ivanov");
        verify(preparedStatementMock, times(1)).setString(3, "ivan@mail.com");
        verify(preparedStatementMock, times(1)).setString(4, "12345");
        verify(preparedStatementMock, times(1)).setString(5, "role");
        verify(preparedStatementMock, times(1)).setString(6, "status");
        verify(preparedStatementMock, times(1)).executeUpdate();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindAll() {
        userDao.findAll();
    }

    @Test
    public void testCountAllRowsByRole() throws SQLException {
        int count = userDao.countAllRowsByRole("role");
        verify(connectionMock, times(1)).prepareStatement(COUNT_ROWS_BY_ROLE);
        verify(preparedStatementMock, times(1)).setString(1, "role");
        verify(preparedStatementMock, times(1)).executeQuery();
        Assert.assertEquals(0, count);
    }

    @Test
    public void testFindAllByRole() throws SQLException {
        when(resultSetMock.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        List<User> allUsers = userDao.findAllByRole("role", 1, 5);
        verify(connectionMock, times(1)).prepareStatement(SELECT_USERS_BY_ROLE);
        verify(preparedStatementMock, times(1)).setString(1, "role");
        verify(preparedStatementMock, times(1)).setInt(2, 1);
        verify(preparedStatementMock, times(1)).setInt(3, 5);
        verify(preparedStatementMock, times(1)).executeQuery();
        verify(resultSetMock, times(1)).getString("first_name");
        verify(resultSetMock, times(1)).getString("last_name");
        verify(resultSetMock, times(1)).getString("email");
        verify(resultSetMock, times(1)).getString("role");
        verify(resultSetMock, times(1)).getString("status");
        verify(resultSetMock, times(1)).getInt("id");
        Assert.assertEquals(1, allUsers.size());
    }

    @Test
    public void testChangeStatus() throws SQLException {
        userDao.changeStatus(1, "changed");
        verify(connectionMock, times(1)).prepareStatement(UPDATE_USER_STATUS);
        verify(preparedStatementMock, times(1)).setString(1, "changed");
        verify(preparedStatementMock, times(1)).setInt(2, 1);
        verify(preparedStatementMock, times(1)).executeUpdate();
    }

    @Test(expected = NotFoundException.class)
    public void testFindById() throws NotFoundException, SQLException {
        userDao.findById(1);
        verify(connectionMock, times(1)).prepareStatement(SELECT_USER_BY_ID);
        verify(preparedStatementMock, times(1)).setInt(1, 1);
        verify(preparedStatementMock, times(1)).executeQuery();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpdate() {
        userDao.update(createTestUser());
    }

    @Test
    public void testDelete() throws UnableToDeleteException, SQLException {
        userDao.delete(1);
        verify(connectionMock, times(1)).prepareStatement(DELETE_USER_BY_ID);
        verify(preparedStatementMock, times(1)).setInt(1, 1);
        verify(preparedStatementMock, times(1)).executeUpdate();
    }

    @Test(expected = UnableToDeleteException.class)
    public void testDeleteThrows() throws UnableToDeleteException, SQLException {
        when(preparedStatementMock.executeUpdate()).thenThrow(new SQLException());
        try {
            userDao.delete(1);
        } catch (UnableToDeleteException e) {
            verify(connectionMock, times(1)).prepareStatement(DELETE_USER_BY_ID);
            verify(preparedStatementMock, times(1)).setInt(1, 1);
            verify(preparedStatementMock, times(1)).executeUpdate();
            throw e;
        }
    }

    @Test(expected = DoesNotExistException.class)
    public void testFindByEmailThrows() throws SQLException, DoesNotExistException {
        userDao.findByEmail("ivanov@mail.com");
        verify(connectionMock, times(1)).prepareStatement(SELECT_USER_BY_EMAIL);
        verify(preparedStatementMock, times(1)).setString(1, "ivanov@mail.com");
        verify(preparedStatementMock, times(1)).executeQuery();
    }

    @Test
    public void testFindByEmail() throws SQLException, DoesNotExistException {
        when(resultSetMock.next()).thenReturn(Boolean.TRUE);
        userDao.findByEmail("ivanov@mail.com");
        verify(connectionMock, times(1)).prepareStatement(SELECT_USER_BY_EMAIL);
        verify(preparedStatementMock, times(1)).setString(1, "ivanov@mail.com");
        verify(preparedStatementMock, times(1)).executeQuery();
        verify(resultSetMock, times(1)).getString("first_name");
        verify(resultSetMock, times(1)).getString("last_name");
        verify(resultSetMock, times(1)).getString("email");
        verify(resultSetMock, times(1)).getString("password");
        verify(resultSetMock, times(1)).getString("role");
        verify(resultSetMock, times(1)).getString("status");
        verify(resultSetMock, times(1)).getInt("id");
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
