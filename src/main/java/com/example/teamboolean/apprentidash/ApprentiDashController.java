package com.example.teamboolean.apprentidash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.security.Principal;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;

@Controller
public class ApprentiDashController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DayRepository dayRepository;

    @GetMapping("/")
    public String getHome(Model m, Principal p){
        //Sets the necessary variables for the nav bar
        loggedInStatusHelper(m, p);
        return "home";
    }

    @GetMapping("/login")
    public String getLogin(Model m, Principal p){
        loggedInStatusHelper(m, p);
        return "login";
    }

    @GetMapping("/signup")
    public String startSignUp(Model m, Principal p){
        loggedInStatusHelper(m, p);
        return "signup";
    }

    @PostMapping("/signup")
    public String addUser(String username, String password, String firstName, String lastName, String managerName){
        AppUser newUser = new AppUser(username, passwordEncoder.encode(password), firstName, lastName, managerName);
        userRepository.save(newUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/";
    }


    //****** The controller methods to handle our Punch In page ******/
    @GetMapping("/recordHour")
    public String recordHour(Model m, Principal p){
        loggedInStatusHelper(m, p);
        return "recordHour";
    }

    @PostMapping(value="/recordHour", params="clockIn=clockInValue")
    public ModelAndView clockInSave() {
        ModelAndView modelAndView = new ModelAndView();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        
        return modelAndView;
    }

    @PostMapping(value="/recordHour", params="lunchIn=lunchInValue")
    public ModelAndView lunchInSave() {
        ModelAndView modelAndView = new ModelAndView();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        return modelAndView;
    }

    @PostMapping(value="/recordHour", params="lunchOut=lunchOutValue")
    public ModelAndView lunchOutSave() {
        ModelAndView modelAndView = new ModelAndView();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        return modelAndView;
    }

    @PostMapping(value="/recordHour", params="clockOut=clockOutValue")
    public ModelAndView clockOutSave() {
        ModelAndView modelAndView = new ModelAndView();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        return modelAndView;
    }




    @GetMapping("/summary")
    public String getSummary(Principal p, Model m){
        loggedInStatusHelper(m, p);
        AppUser currentUser = userRepository.findByUsername(p.getName());
        m.addAttribute("localDate", LocalDate.now());
        m.addAttribute("user", currentUser);
        return "summary";
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

}
