package com.example.teamboolean.apprentidash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.security.Principal;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;

@Controller
public class TimesheetController {
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
    DayRepository dayRepository;

    Day currentDay = new Day();

    /********************************* The controller methods to handle our Punch In page **************************************************************/
    @GetMapping("/recordHour")
    public String recordHour(Model m, Principal p){
        //Sets the necessary variables for the nav bar
        loggedInStatusHelper(m, p);
        m.addAttribute("currentPage", "clock_in");
        //Sets status for knowing which button to show
        LocalDateTime now = LocalDateTime.now();
        AppUser currentUser = userRepository.findByUsername(p.getName());
        m.addAttribute("workStatus", buttonRenderHelper(currentUser));
        m.addAttribute("todayDate", now);
        return "recordHour";
    }

    //Route to handle our clock in button
    @PostMapping(value="/recordHour", params="name=value")
    public String clockInSave(Principal p, Model m) {

        AppUser currentUser = userRepository.findByUsername(p.getName());

        if(buttonRenderHelper(currentUser).equals("clockIn")) {
            currentUser.getCurrentday().setClockIn(LocalDateTime.now());
        }else if(buttonRenderHelper(currentUser).equals(("lunchIn"))) {
            currentUser.getCurrentday().setLunchStart(LocalDateTime.now());
        }else if(buttonRenderHelper(currentUser).equals("lunchOut")) {
            currentUser.getCurrentday().setLunchEnd(LocalDateTime.now());
        }else if(buttonRenderHelper(currentUser).equals("clockOut")){
            currentUser.getCurrentday().setClockOut(LocalDateTime.now());
        }

        currentUser.getCurrentday().setUser(currentUser);
        dayRepository.save(currentUser.getCurrentday());
        m.addAttribute("workStatus", buttonRenderHelper(currentUser));
        return "redirect:/recordHour";
    }

    public String buttonRenderHelper(AppUser currentUser ){
        if(currentUser.getCurrentday() == null) {
            Day day = new Day();
            currentUser.setCurrentday(day);
        }
        if(currentUser.getCurrentday().getClockIn() == null)
            return "clockIn";
        else if(currentUser.getCurrentday().getLunchStart() == null)
            return "lunchIn";
        else if(currentUser.getCurrentday().getLunchEnd() == null)
            return "lunchOut";
        else if(currentUser.getCurrentday().getClockOut() == null)
            return "clockOut";
        else
            return "notNewDay";
    }

    @GetMapping ("/additionalDayRecord")
    public RedirectView makeDay(Principal p){
        AppUser currentUser = userRepository.findByUsername(p.getName());
        currentUser.setCurrentday(null);
        userRepository.save(currentUser);
        return new RedirectView("/recordHour");
    }

/******************************** End of the controller for handle Punch In page ********************************************************************/


/******************************** Summary Route ******************************************************************************/
    @GetMapping("/summary")
    public String getSummary(Principal p, Model m, String fromDate, String toDate){
        //Sets the necessary variables for the nav bar
        loggedInStatusHelper(m, p);
        m.addAttribute("currentPage", "summary");

        //Retrieve info of logged in user
        AppUser currentUser = userRepository.findByUsername(p.getName());

        //Add to the model for display
        m.addAttribute("user", currentUser);

        //Get associated days of the user
        List<Day> userDays = currentUser.days;

        //initialize list
        dateRange = new ArrayList<>();

        //Get first day of the current week
        LocalDate from = getFirstDay();
        //Get last day of current week
        LocalDate to = getLastDay();

        //Check if input dates are not null, if not convert into local date
        if (fromDate != null){
            from = LocalDate.parse(fromDate);
        }

        if (toDate != null){
            to = LocalDate.parse(toDate);
        }

        //Current work hours so far
        totalHours = 0.00;
        // retrieves the days based from date range and compute the
        // total working hours
        for (Day curDay: userDays){
            LocalDate local = curDay.clockIn.toLocalDate();

            if (local.compareTo(from) >= 0 && local.compareTo(to)<= 0){
                dateRange.add(curDay);
                totalHours += curDay.calculateDailyHours();
            }
        }

        //Sort the list by clock-in dates
        sortDateList();

        //Add to model for display in summary.html
        m.addAttribute("fromDate", from);
        m.addAttribute("toDate", to);
        m.addAttribute("days", dateRange);
        m.addAttribute("totalHours", totalHours);
        return "summary";
    }


    /************************************ Controller to handle the Edit page ***************************************************************************/
    @GetMapping("/edit/{dayId}")
    public String getEdit(@PathVariable long dayId, Model m, Principal p) {
        //Sets the necessary variables for the nav bar
        loggedInStatusHelper(m, p);
        m.addAttribute("currentPage", "clock_in");

        Day currentDay = dayRepository.findById(dayId).get();
        AppUser currentUser = userRepository.findByUsername(p.getName());
        if(!currentUser.days.contains(currentDay))
            return "error";
        else{
            m.addAttribute("currentDay", currentDay);
            return "edit";
        }
    }

    @PostMapping("/edit")
    public String postEdit(long dayId,String clockIn, String clockOut, String lunchStart, String lunchEnd){
        Day currentDay = dayRepository.findById(dayId).get();
        LocalTime clockInLocalTime = LocalTime.parse(clockIn);
        LocalTime clockOutLocalTime = LocalTime.parse(clockOut);
        LocalTime lunchStartLocalTime = LocalTime.parse(lunchStart);
        LocalTime lunchEndLocalTime = LocalTime.parse(lunchEnd);

        currentDay.setClockIn(currentDay.getClockIn().withHour(clockInLocalTime.getHour()).withMinute(clockInLocalTime.getMinute()));
        currentDay.setClockOut(currentDay.getClockOut().withHour(clockOutLocalTime.getHour()).withMinute(clockOutLocalTime.getMinute()));
        currentDay.setLunchStart(currentDay.getLunchStart().withHour(lunchStartLocalTime.getHour()).withMinute(lunchStartLocalTime.getMinute()));
        currentDay.setLunchEnd(currentDay.getLunchEnd().withHour(lunchEndLocalTime.getHour()).withMinute(lunchEndLocalTime.getMinute()));

        dayRepository.save(currentDay);

        return "redirect:/summary";
    }
    //TODO Fix the bug that doesnt let me delete day instances that have not been clocked out yet
    @GetMapping("/delete/{dayId}")
    public String deleteDay(@PathVariable long dayId, Principal p){
        Day currentDay = dayRepository.findById(dayId).get();
        AppUser currentUser = userRepository.findByUsername(p.getName());
        if(!currentUser.days.contains(currentDay))
            return "error";
        else{
            dayRepository.delete(currentDay);
            return "redirect:/summary";
        }

    }

    /************************************ End of Controller to handle the Edit page ***************************************************************************/


    /***************************** CSV CONTROLLER ***************************/

    @GetMapping("/timesheet")
    public void exportCSV(HttpServletResponse response) throws Exception {

        //set file name and content type
        String filename = "timesheet.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //Write to file and download
        PrintWriter csvWriter = response.getWriter();

        String header = "Day,Date,Time In,Time Out,Lunch,Daily Hours";
        csvWriter.println(header);

        for(Day curDay: dateRange){

            csvWriter.println(curDay.toString());
        }

        csvWriter.println(",,,,Total Hours:," + totalHours);
        csvWriter.close();

    }


    /******************************** All the helper function ************************************/

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

    //Helper function to get the first day
    //Reference: https://stackoverflow.com/questions/22890644/get-current-week-start-and-end-date-in-java-monday-to-sunday
    private LocalDate getFirstDay(){
        firstDay = WeekFields.of(Locale.US).getFirstDayOfWeek();
        return LocalDate.now(USZONE).with(TemporalAdjusters.previousOrSame(firstDay));
    }

    //Helper function to get the last day
    //Reference: https://stackoverflow.com/questions/22890644/get-current-week-start-and-end-date-in-java-monday-to-sunday
    private LocalDate getLastDay(){
        DayOfWeek lastDay = DayOfWeek.of(((firstDay.getValue() + 5) % DayOfWeek.values().length) + 1);
        return LocalDate.now(USZONE).with(TemporalAdjusters.nextOrSame(lastDay));

    }

    //Sort dates from earliest to latest
    //Ref: http://java-buddy.blogspot.com/2013/01/sort-list-of-date.html
    private void sortDateList(){
        Collections.sort(dateRange, new Comparator<Day>(){

            @Override
            public int compare(Day o1, Day o2) {
                return o1.clockIn.compareTo(o2.clockIn);
            }
        });
    }


}
