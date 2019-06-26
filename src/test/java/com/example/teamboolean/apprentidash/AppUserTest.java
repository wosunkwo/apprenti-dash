package com.example.teamboolean.apprentidash;

import org.junit.Test;

import static org.junit.Assert.*;

public class AppUserTest {

    @Test
    public void appUserNull(){
        AppUser test = new AppUser();
        assertNull("this should be null", test.username);
        assertNull("this should be null", test.password);
        assertNull("this should be null", test.firstName);
        assertNull("this should be null", test.lastName);
        assertNull("this should be null", test.managerName);
    }

    @Test
    public void appUserCreated(){

        String userName = "itsName";
        String password = "test";
        String firstName = "FirstName";
        String lastName = "LastName";
        String managerName = "ManagerName";

        AppUser test = new AppUser(userName, password, firstName, lastName, managerName);


        assertEquals("should return instance of a username", "itsName", test.getUsername());
        assertEquals("should return instance of a password", "test", test.getPassword());
        assertEquals("should return instance of a first name", "FirstName", test.getFirstName());
        assertEquals("should return instance of a last name", "LastName", test.getLastName());
        assertEquals("should return instance of a manager name", "ManagerName", test.getManagerName());

    }

    @Test
    public void appUserMissingFields(){

        String userName = "itsName";
        String password = "test";
        String firstName = "FirstName";
        String lastName = "LastName";

        AppUser test = new AppUser(userName, password, firstName, lastName, "");


        assertEquals("should return instance of a username", "itsName", test.getUsername());
        assertEquals("should return instance of a password", "test", test.getPassword());
        assertEquals("should return instance of a first name", "FirstName", test.getFirstName());
        assertEquals("should return instance of a last name", "LastName", test.getLastName());
        assertEquals("should return instance of a manager name", "", test.getManagerName());
    }
}