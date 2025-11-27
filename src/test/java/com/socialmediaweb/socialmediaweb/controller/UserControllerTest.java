package com.socialmediaweb.socialmediaweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.socialmediaweb.socialmediaweb.entities.Users;
import com.socialmediaweb.socialmediaweb.repository.UsersRepository;
import com.socialmediaweb.socialmediaweb.service.AuthenticationService;

public class UserControllerTest {

    private AuthenticationService authService;
    private UserController userController;
    private UsersRepository userRepository;

    @BeforeEach
    public void setup() {
        authService = mock(AuthenticationService.class);
        userRepository = mock(UsersRepository.class);
        userController = new UserController(authService);
    }

    @Test
    public void testCreateUser_Success() {
        Users user = new Users("testUser", "test@example.com", "password");
        when(authService.isUsernameExists("testUser")).thenReturn(false);
        when(authService.isEmailExists("test@example.com")).thenReturn(false);
        when(authService.saveUser(any(Users.class))).thenReturn(user);

        ResponseEntity<String> response = userController.createUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User has registered successfully!", response.getBody());
    }

    @Test
    public void testUserGetterSetter() {
        Users user = new Users();
        user.setUserId(1L);
        user.setUsername("testUser");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@example.com");
        user.setPasswordHash("password");
        // Note: Gender field doesn't exist in Users entity, removing this line
        user.setProfilePicture("profile.jpg");
        Date createdOn = new Date();
        user.setCreatedAt(createdOn.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
        user.setAdmin(true);

        assertEquals(1L, user.getUserId());
        assertEquals("testUser", user.getUsername());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPasswordHash());
        // Note: Gender field doesn't exist, removing assertion
        assertEquals("profile.jpg", user.getProfilePicture());
        assertEquals(createdOn.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(), user.getCreatedAt());
        assertEquals(true, user.getAdmin());
    }

    @Test
    public void testAuthenticateUser_Success() {
        Users user = new Users("testUser", "test@example.com", "password");
        when(authService.authenticateUser("testUser", "password")).thenReturn(user);

        Users authenticatedUser = authService.authenticateUser("testUser", "password");

        assertEquals(user, authenticatedUser);
    }
}
//    @Test
//    public void testEndToEndScenario() {
//        // Set the path to the chromedriver executable
//        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
//
//        // Initialize the ChromeDriver
//        WebDriver driver = new ChromeDriver();
//
//        // Navigate to the application URL
//        driver.get("http://localhost:8080");
//
//        // Find the username and password input fields and enter credentials
//        WebElement usernameInput = driver.findElement(By.id("username"));
//        WebElement passwordInput = driver.findElement(By.id("password"));
//        usernameInput.sendKeys("testUser");
//        passwordInput.sendKeys("password");
//
//        // Find the login button and click it
//        WebElement loginButton = driver.findElement(By.id("loginButton"));
//        loginButton.click();
//
//        // Find a specific element on the page and assert its presence
//        WebElement welcomeMessage = driver.findElement(By.id("welcomeMessage"));
//        assertEquals("Welcome, testUser!", welcomeMessage.getText());
//
//        // Close the browser
//        driver.quit();
//    }
//}

//    @Test
//    public void testFindByUsernameContainingIgnoreCase() {
//        List<String> usernames = new ArrayList<>();
//        usernames.add("testUser1");
//        usernames.add("testUser2");
//        usernames.add("testUser3");
//
//        when(userRepository.findByUsernameContainingIgnoreCase("test")).thenReturn(usernames);
//
//        List<String> result = userController.findUsernamesContaining("test");
//
//        assertEquals(usernames, result);
//    }

