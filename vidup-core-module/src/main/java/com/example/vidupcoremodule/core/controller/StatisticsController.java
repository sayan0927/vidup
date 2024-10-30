package com.example.vidupcoremodule.core.controller;


import com.example.vidupcoremodule.core.View_DTO.DTOService;
import com.example.vidupcoremodule.core.View_DTO.StatisticsPageDTO;
import com.example.vidupcoremodule.core.entity.LoggedInUserDetails;
import com.example.vidupcoremodule.core.service.userservices.UserService;
import com.example.vidupcoremodule.core.util.UtilClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {



    @Autowired
    DTOService dtoService;

    @Autowired
    UtilClass utilClass;


    @Autowired
    UserService userService;

    /**
     * Retrieves the statistics page in HTML format for a specific user.
     * @param userId The ID of the user whose statistics are to be fetched.
     * @param authentication The authentication object containing the details of the logged-in user.
     * @return A ModelAndView object containing the user's statistics page.
     */
    @GetMapping("/{userId}/page")
    public ModelAndView usersStatisticsHTML(@PathVariable("userId") String userId,Authentication authentication)
    {
        if(!userService.userIdExists(userId))
        {
            return new ModelAndView("error").addObject("Message", "Invalid userID");
        }

        LoggedInUserDetails loggedInUser = utilClass.getUserDetailsFromAuthentication(authentication);
        StatisticsPageDTO statisticsPageDTO = dtoService.getStatisticsOfUser(loggedInUser.getUser());

        ModelAndView m = utilClass.getHTMLofLoggedInUser("statistics", loggedInUser);
        m.addObject("dto", statisticsPageDTO);
        return m;
    }

    /**
     * Retrieves the statistics in JSON format for a specific user.
     * @param userId The ID of the user whose statistics are to be fetched.
     * @param authentication The authentication object containing the details of the logged-in user.
     * @return A ResponseEntity object containing the user's statistics.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> usersStatisticsJSON(@PathVariable("userId") String userId,Authentication authentication)
    {
        if(!userService.userIdExists(userId))
            return new ResponseEntity<>("Invalid UserId",HttpStatus.NOT_FOUND);


        StatisticsPageDTO statisticsPageDTO = dtoService.getStatisticsOfUser(userService.getUserById(userId));
        return new ResponseEntity<>(statisticsPageDTO,HttpStatus.OK);
    }

    /**
     * Retrieves the statistics page in HTML format for the logged-in user.
     * @param authentication The authentication object containing the details of the logged-in user.
     * @return A ModelAndView object containing the logged-in user's statistics page.
     */
    @GetMapping("/my/page")
    public ModelAndView myStatisticsHTML(Authentication authentication) {

        LoggedInUserDetails loggedInUserDetails = utilClass.getUserDetailsFromAuthentication(authentication);
        String userId = loggedInUserDetails.getUser().getId().toString();

        return new ModelAndView("forward:/statistics/"+userId+"/page");

    }

    /**
     * Retrieves the statistics in JSON format for the logged-in user.
     * @param authentication The authentication object containing the details of the logged-in user.
     * @return A ResponseEntity object containing the logged-in user's statistics.
     */
    @GetMapping("/my")
    public ResponseEntity<?> myStatisticsJSON(Authentication authentication) {
        StatisticsPageDTO statisticsPageDTO = dtoService.getStatisticsOfUser(utilClass.getUserDetailsFromAuthentication(authentication).getUser());
        return new ResponseEntity<>(statisticsPageDTO, HttpStatus.OK);
    }
}
