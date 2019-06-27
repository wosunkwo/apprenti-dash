package com.example.teamboolean.apprentidash;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.time.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ApprentiDashController {
    //US Zone ID
    private final static ZoneId USZONE = ZoneId.of("America/Los_Angeles");
    //first Day of the week
    private DayOfWeek firstDay;
    //Day list based from date range
    private List<Day> dateRange;
    //total hours worked
    private double totalHours;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DayRepository dayRepository;

    Day currentDay = new Day();

    //Root route
    @GetMapping("/")
    public RedirectView getRoot(Model m, Principal p){

        // If the user is logged in, redirect them to clock-in
        // otherwise, direct them to home page
        // Huge thanks to David for the idea!
        if(p != null){
            return new RedirectView("/recordHour");
        } else {
            return new RedirectView("/home");
        }
    }

    //Home page
    @GetMapping("/home")
    public String getHome(Model m, Principal p){
        //Sets the necessary variables for the nav bar
        loggedInStatusHelper(m, p);
        m.addAttribute("currentPage", "home");
        return "home";
    }

    //Login Page
    @GetMapping("/login")
    public String getLogin(Model m, Principal p){
        //Sets the necessary variables for the nav bar
        loggedInStatusHelper(m, p);
        m.addAttribute("currentPage", "login");
        return "login";
    }

    //Sign-up page
    @GetMapping("/signup")
    public String startSignUp(Model m, Principal p){
        //Sets the necessary variables for the nav bar
        loggedInStatusHelper(m, p);
        m.addAttribute("currentPage", "signup");
        return "signup";
    }

    @PostMapping("/signup")
    public String addUser(String username, String password, String firstName, String lastName, String managerName){
        if (!checkUserName(username)) {
            AppUser newUser = new AppUser(username, passwordEncoder.encode(password), firstName, lastName, managerName);
            userRepository.save(newUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/";
        }else {
            return "duplicateUsername";
        }
    }






    //Checks if the user is logged in and sets the model attributes accordingly per the navbar requirements
    private void loggedInStatusHelper(Model m, Principal p){

        //Navbar required variables for knowing if user is logged in and their name for display
        boolean isLoggedIn;
        String currentUserFirstName;

        //Check if the user is logged in and sets the variables
        if(p == null){
            isLoggedIn = false;
            currentUserFirstName = "Visitor";
        }else {
            isLoggedIn = true;
            currentUserFirstName = userRepository.findByUsername(p.getName()).getFirstName();
        }

        //add the attributes to the passed in model
        m.addAttribute("isLoggedIn", isLoggedIn);
        m.addAttribute("userFirstName", currentUserFirstName);
    }

    //help function to check if the username exist in database
    public boolean checkUserName(String username){
        Iterable<AppUser> allUsers =  userRepository.findAll();
        List<String> allUsername = new ArrayList<>();

        for(AppUser appUser : allUsers){
            allUsername.add(appUser.username);
        }

        if(allUsername.contains(username)){
            return true;
        }else{
            return false;
        }
    }

}
