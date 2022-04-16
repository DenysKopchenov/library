package com.dkop.library.services;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.OrderDao;
import com.dkop.library.dao.UserDao;
import com.dkop.library.entity.User;
import com.dkop.library.exceptions.AlreadyExistException;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToDeleteException;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static com.dkop.library.controller.command.CommandUtils.messagesBundle;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserService userService;
    private DaoFactory daoFactoryMock = mock(DaoFactory.class);
    private UserDao userDaoMock = mock(UserDao.class);

    @Before
    public void setUp() {
        try (MockedStatic<DaoFactory> daoFactoryMockedStatic = mockStatic(DaoFactory.class)) {
            daoFactoryMockedStatic.when(DaoFactory::getInstance).thenReturn(daoFactoryMock);
            when(daoFactoryMock.createUserDao()).thenReturn(userDaoMock);
            messagesBundle = mock(ResourceBundle.class);
            userService = new UserService(DaoFactory.getInstance());
        }
    }

    @Test
    public void testFindById() throws NotFoundException {
        when(userDaoMock.findById(1)).thenReturn(User.newBuilder().build());
        User foundedUser = userService.findById(1);
        verify(daoFactoryMock, times(1)).createUserDao();
        verify(userDaoMock, times(1)).findById(1);
        Assert.assertEquals(User.newBuilder().build(), foundedUser);
    }

    @Test(expected = NotFoundException.class)
    public void testFindByIdThrows() throws NotFoundException {
        when(userDaoMock.findById(1)).thenThrow(NotFoundException.class);
        try {
            userService.findById(1);
        } catch (NotFoundException e) {
            verify(daoFactoryMock, times(1)).createUserDao();
            verify(userDaoMock, times(1)).findById(1);
            throw e;
        }
    }

    @Test
    public void testFindAllByRole() {
        List<User> usersByRole = userService.findAllByRole("role", 1, 5);
        verify(daoFactoryMock, times(1)).createUserDao();
        verify(userDaoMock, times(1)).findAllByRole("role", 1, 5);
        Assert.assertTrue(usersByRole.isEmpty());
    }

    @Test
    public void testGetUserInfo() throws DoesNotExistException {
        when(userDaoMock.findByEmail("mail@mail.com")).thenReturn(User.newBuilder().build());
        User foundedUser = userService.getUserInfo("mail@mail.com");
        verify(daoFactoryMock, times(1)).createUserDao();
        verify(userDaoMock, times(1)).findByEmail("mail@mail.com");
        Assert.assertEquals(User.newBuilder().build(), foundedUser);
    }

    @Test(expected = DoesNotExistException.class)
    public void testGetUserInfoThrows() throws DoesNotExistException {
        when(userDaoMock.findByEmail("mail@mail.com")).thenThrow(DoesNotExistException.class);
        try {
            userService.getUserInfo("mail@mail.com");
        } catch (DoesNotExistException e) {
            verify(daoFactoryMock, times(1)).createUserDao();
            verify(userDaoMock, times(1)).findByEmail("mail@mail.com");
            throw e;
        }
    }

    @Test
    public void testCreateUser() throws AlreadyExistException, SQLException {
        userService.createUser("Ivan",
                "Ivanov",
                "mail@mail.com",
                "12345",
                "role",
                "status");
        verify(daoFactoryMock, times(1)).createUserDao();
        verify(userDaoMock, times(1)).create(User.newBuilder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("mail@mail.com")
                .password(DigestUtils.sha256Hex("12345"))
                .role("role")
                .status("status")
                .build());
    }

    @Test(expected = AlreadyExistException.class)
    public void testCreateUserThrows() throws SQLException, AlreadyExistException {
        User testUser = User.newBuilder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("mail@mail.com")
                .password(DigestUtils.sha256Hex("12345"))
                .role("role")
                .status("status")
                .build();
        doThrow(SQLException.class).when(userDaoMock).create(testUser);
        try {
            userService.createUser("Ivan",
                    "Ivanov",
                    "mail@mail.com",
                    "12345",
                    "role",
                    "status");
        } catch (AlreadyExistException e) {
            verify(daoFactoryMock, times(1)).createUserDao();
            verify(userDaoMock, times(1)).create(User.newBuilder()
                    .firstName("Ivan")
                    .lastName("Ivanov")
                    .email("mail@mail.com")
                    .password(DigestUtils.sha256Hex("12345"))
                    .role("role")
                    .status("status")
                    .build());
            throw e;
        }
    }

    @Test
    public void testDeleteUser() throws UnableToDeleteException, NotFoundException {
        userService.deleteUser(1);
        verify(daoFactoryMock, times(1)).createUserDao();
        verify(userDaoMock, times(1)).findById(1);
        verify(userDaoMock, times(1)).delete(1);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteUserThrowsNotFound() throws UnableToDeleteException, NotFoundException {
        when(userDaoMock.findById(1)).thenThrow(NotFoundException.class);
        try {
            userService.deleteUser(1);
        } catch (NotFoundException e) {
            verify(daoFactoryMock, times(1)).createUserDao();
            verify(userDaoMock, times(1)).findById(1);
            throw e;
        }
    }

    @Test(expected = UnableToDeleteException.class)
    public void testDeleteUserThrowsUnableToDelete() throws UnableToDeleteException, NotFoundException {
        doThrow(UnableToDeleteException.class).when(userDaoMock).delete(1);
        try {
            userService.deleteUser(1);
        } catch (UnableToDeleteException e) {
            verify(daoFactoryMock, times(1)).createUserDao();
            verify(userDaoMock, times(1)).findById(1);
            verify(userDaoMock, times(1)).delete(1);
            throw e;
        }
    }

    @Test
    public void testChangeStatus() throws NotFoundException, SQLException {
        userService.changeStatus(1, "newStatus");
        verify(daoFactoryMock, times(1)).createUserDao();
        verify(userDaoMock, times(1)).findById(1);
        verify(userDaoMock, times(1)).changeStatus(1, "newStatus");
    }

    @Test(expected = NotFoundException.class)
    public void testChangeStatusThrows() throws NotFoundException {
        when(userDaoMock.findById(1)).thenThrow(NotFoundException.class);
        try {
            userService.changeStatus(1, "newStatus");
        } catch (NotFoundException e) {
            verify(daoFactoryMock, times(1)).createUserDao();
            verify(userDaoMock, times(1)).findById(1);
            throw e;
        }
    }

    @Test
    public void testCountAllRowsByRole() {
        int count = userService.countAllRowsByRole("role");
        verify(daoFactoryMock, times(1)).createUserDao();
        verify(userDaoMock, times(1)).countAllRowsByRole("role");
        Assert.assertEquals(0 ,count);
    }
}