package com.example.teamboolean.apprentidash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
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

    Day currentDay = new Day();

    @GetMapping("/")
    public String getHome(Model m, Principal p){

        //Check if the user is logged in and pass the user info to the model
        boolean isLoggedIn;
        String currentUserFirstName;
        if(p == null){
            isLoggedIn = false;
            currentUserFirstName = "Visitor";
        }else {
            isLoggedIn = true;
            currentUserFirstName = userRepository.findByUsername(p.getName()).getFirstName();
        }
        m.addAttribute("isLoggedIn", isLoggedIn);
        m.addAttribute("userFirstName", currentUserFirstName);

        return "home";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }

    @GetMapping("/signup")
    public String startSignUp(){
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
    public String recordHour(){
        return "recordHour";
    }


//Route to handle our clock in button
    @PostMapping(value="/recordHour", params="clockIn=clockInValue")
    public ModelAndView clockInSave(Principal p) {
        ModelAndView modelAndView = new ModelAndView();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        currentDay.setClockIn(now);
        currentDay.setUser(userRepository.findByUsername(p.getName()));
        dayRepository.save(currentDay);

        return modelAndView;
    }

    //Route to handle our Lunch in button
    @PostMapping(value="/recordHour", params="lunchIn=lunchInValue")
    public ModelAndView lunchInSave(Principal p) {
        ModelAndView modelAndView = new ModelAndView();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();


        currentDay.setLunchStart(now);
        currentDay.setUser(userRepository.findByUsername(p.getName()));
        dayRepository.save(currentDay);

        return modelAndView;
    }

    //Route to handle our lunch out button
    @PostMapping(value="/recordHour", params="lunchOut=lunchOutValue")
    public ModelAndView lunchOutSave(Principal p) {
        ModelAndView modelAndView = new ModelAndView();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        currentDay.setLunchEnd(now);
        currentDay.setUser(userRepository.findByUsername(p.getName()));
        dayRepository.save(currentDay);
        return modelAndView;
    }

    //Route to handle our clock out button
    @PostMapping(value="/recordHour", params="clockOut=clockOutValue")
    public ModelAndView clockOutSave(Principal p) {
        ModelAndView modelAndView = new ModelAndView();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        
        currentDay.setClockOut(now);
        currentDay.setUser(userRepository.findByUsername(p.getName()));
        dayRepository.save(currentDay);

        return modelAndView;
    }

//    public String buttonRenderHelper(){
//        dayRepository.findById( )
//    }

//**************** End of the controller for handle Punch In page *************************//


    @GetMapping("/summary")
    public String getSummary(Principal p, Model m){
        AppUser currentUser = userRepository.findByUsername(p.getName());
        m.addAttribute("localDate", LocalDate.now());
        m.addAttribute("user", currentUser);
        return "summary";
    }

}
