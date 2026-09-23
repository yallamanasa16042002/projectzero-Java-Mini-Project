package com.instagram.service;

import com.instagram.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Test
    void registerUser() {
        UserService userService = new UserServiceImpl();

        User user = new User();

        user.setUsername("Manasa6");
        user.setEmail("Manasa6@gmail.com");
        user.setPasswordHash("password1231");
        user.setStatus("ACTIVE");
        user.setRole("USER");

        boolean result = userService.registerUser(user);
        assertTrue(result);
    }


    @Test
    void login() {

        UserService userService = new UserServiceImpl();

        User user = userService.login("Manasa6", "password1231");

        assertNotNull(user);
        assertEquals("Manasa6", user.getUsername());

        System.out.println(
                "Login Successful: " +
                        user.getUserId() + " " +
                        user.getUsername() + " " +
                        user.getEmail()
        );

    }

    @Test
    void getUserById() {
        UserService userService = new UserServiceImpl();

        User user = userService.getUserById(6);

        assertNotNull(user);
        assertEquals(6, user.getUserId());

        System.out.println(
                user.getUserId() + " " +
                        user.getUsername() + " " +
                        user.getEmail()
        );
    }

    @Test
    void getAllUsers() {
        UserService userService = new UserServiceImpl();

        List<User> users = userService.getAllUsers();

        assertNotNull(users);

        for (User user : users) {
            System.out.println(
                    user.getUserId() + " " +
                            user.getUsername() + " " +
                            user.getEmail()
            );
        }

    }

    @Test
    void updateUser() {
        UserService userService = new UserServiceImpl();

        User user = userService.getUserByUsername("Manasa1");

        assertNotNull(user);

        user.setEmail("ManasaUpdated@gmail.com");
        user.setPasswordHash("newpassword123");
        user.setStatus("ACTIVE");
        user.setRole("USER");

        boolean result = userService.updateUser(user);

        assertTrue(result);

        System.out.println("User Updated Successfully");

    }

    @Test
    void deactivateUser() {
    }

    @Test
    void getUserByUsername() {

        UserServiceImpl userserver= new UserServiceImpl();
    User user=userserver.getUserByUsername("Manasa");
    assertNotNull(user);
    assertEquals("Manasa",user.getUsername());
    System.out.println(user.getUserId()+" "+user.getUsername()+" "+user.getEmail());

    }

    @Test
    void deleteUser(){

        UserService userService = new UserServiceImpl();

        boolean result = userService.deleteuser(4);

        assertTrue(result);
        System.out.println("Deleted Successfully");
    }
}