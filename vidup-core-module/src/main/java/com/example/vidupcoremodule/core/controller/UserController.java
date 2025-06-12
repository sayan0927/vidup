package com.example.vidupcoremodule.core.controller;



import com.example.vidupcoremodule.CoreApplicationConstants;
import com.example.vidupcoremodule.core.View_DTO.DTOService;
import com.example.vidupcoremodule.core.View_DTO.ProfileDetailsDTO;
import com.example.vidupcoremodule.core.View_DTO.VideoDTO;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.SignupRequest;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.LoggedInUserDetails;
import com.example.vidupcoremodule.core.service.userservices.SignupService;
import com.example.vidupcoremodule.core.service.userservices.UserService;
import com.example.vidupcoremodule.core.util.UtilClass;
import com.example.vidupcoremodule.storage.LocalStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Controller
@RequestMapping("/users")
public class UserController {




    @Autowired
    UserService userService;

    @Autowired
    SignupService signupService;


    @Autowired
    UtilClass utilClass;


    @Autowired
    DTOService dtoService;

    LocalStorageService storageService;
    public UserController(LocalStorageService storageService) {
        this.storageService = storageService;
    }





    /**
     * Retrieves the profile image of a user by user ID.
     * @param userId    User ID
     * @return          Profile image in byte array format , blank image(HTTP 404) if userId does not exist
     * @throws IOException if an I/O error occurs
     */
    @GetMapping(value = "/permitted/{userId}/profile_img", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getProfileImageById(@PathVariable("userId") String userId) throws IOException {

        if(!userService.userIdExists(userId))
        {
            byte[] img = storageService.readFileAsBytesFromClassPath(CoreApplicationConstants.blankUserImage);
            return new ResponseEntity<>(img,HttpStatus.NOT_FOUND);
        }
        byte[] imageContent = userService.getProfileImage(Integer.parseInt(userId));
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    /**
     * Retrieves the profile image of a user by username.
     * @param userName  Username
     * @return          Profile image in byte array format , blank image(HTTP 404) if userName does not exist
     * @throws IOException if an I/O error occurs
     */
    @GetMapping(value = "/permitted/{userName}/profile_img_by_uname", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getProfileImageByUserName(@PathVariable("userName") String userName) throws IOException {


        if(!userService.usernameExists(userName))
        {
            byte[] img = storageService.readFileAsBytesFromClassPath(CoreApplicationConstants.blankUserImage);
            return new ResponseEntity<>(img,HttpStatus.NOT_FOUND);
        }
        byte[] imageContent = userService.getProfileImage(userName);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }




    /**
     * Returns the account page for the authenticated user.
     * @param authentication    Authentication object for the logged-in user
     * @return                  ModelAndView for the account page
     */
    @GetMapping("/my_account")
    public ModelAndView accountPage(Authentication authentication) {
        LoggedInUserDetails loggedInUser = utilClass.getUserDetailsFromAuthentication(authentication);
        ModelAndView modelAndView = utilClass.getHTMLofLoggedInUser("account", loggedInUser);
        modelAndView.addObject("user_details", loggedInUser);
        modelAndView.addObject("dto", new ProfileDetailsDTO());
        modelAndView.setStatus(HttpStatusCode.valueOf(200));
        return modelAndView;

    }

    /**
     * Returns the user's channel information in JSON format.
     * @param creatorId     User ID of the channel creator
     * @param authentication    Authentication object for the logged-in user
     * @return              ResponseEntity containing channel information or error message
     */
    @GetMapping("/{userId}/channel")
    public ResponseEntity<?> usersChannelJSON(@PathVariable("userId") String creatorId, Authentication authentication) {

        User user = userService.getUserById(creatorId);
        if(user == null) {
            return new ResponseEntity<>("User does not exist",HttpStatus.NOT_FOUND);
        }
        List<VideoDTO> dtoList = dtoService.getUsersVideosDTO(user);
        Map<String,Object> model = new HashMap<>();
        model.put("channel_user", user);
        model.put("dto", dtoList);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    /**
     * Returns the user's channel page in HTML format.
     * @param creatorId     User ID of the channel creator
     * @param authentication    Authentication object for the logged-in user
     * @return              ModelAndView for the channel page
     */
    @GetMapping("/{userId}/channel/page")
    public ModelAndView usersChannelHTML(@PathVariable("userId") String creatorId, Authentication authentication) {

        User user = userService.getUserById(creatorId);
        if(user == null) {
            return new ModelAndView("error").addObject("message", "Invalid userId");
        }

        List<VideoDTO> dtoList = dtoService.getUsersVideosDTO(user);
        ModelAndView modelAndView = utilClass.getHTMLofLoggedInUser("channel", utilClass.getUserDetailsFromAuthentication(authentication));
        modelAndView.addObject("dto", dtoList);
        modelAndView.addObject("channel_user", user);
        modelAndView.setStatus(HttpStatusCode.valueOf(200));
        return modelAndView;


    }


    /**
     * Updates the profile of the authenticated user.
     * @param authentication    Authenticated User
     * @param profileDetails    Details to update
     * @param multipartFile     Profile image file
     * @return                  Updated user information or error message
     */
    @CrossOrigin(origins = "*")
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(Authentication authentication, ProfileDetailsDTO profileDetails, @RequestParam(value = "profile_image", required = false) MultipartFile multipartFile) {

        /*
        Currently Email is Not Allowed to be updated
        Since Email is Not Allowed to be updated, these checks are not needed now
        if (!profileDetails.emailEmpty())
        {
            if (userService.emailExists(profileDetails.getEmail()) || signupService.emailSubmittedForRegistration(profileDetails.getEmail()))
                return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
            if (!signupService.validEmailAddress(profileDetails.getEmail()))
                return new ResponseEntity<>("Invalid email address", HttpStatus.BAD_REQUEST);
        }
         */
        profileDetails.setEmail(null);

        if (!multipartFile.isEmpty() && !utilClass.isMIMETypeImage(multipartFile)) {
            return new ResponseEntity<>("Provided Profile Image is Not Image File", HttpStatus.BAD_REQUEST);
        }

        if (!profileDetails.getPassword().equals(profileDetails.getPasswordConfirm()))
            return new ResponseEntity<>("Passwords do not match", HttpStatus.BAD_REQUEST);

        try {
            User updated = userService.updateUser(profileDetails, multipartFile, utilClass.getUserDetailsFromAuthentication(authentication).getUser());

            if (updated == null) return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);

            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Returns the activation page for user registration.
     * This page is sent to users email.
     * @param userName  Username to activate
     * @param token     Activation token
     * @return          ModelAndView for the activation page
     */
    @GetMapping("/register/activate/page")
    public ModelAndView getActivationPage(@RequestParam(value = "uname") String userName, @RequestParam("token") String token) {
        ModelAndView modelAndView = new ModelAndView("activate_account"); // assuming activateAccount is the name of your view
        modelAndView.addObject("userName", userName);
        modelAndView.addObject("token", token);
        return modelAndView;
    }

    /**
     * Activates a user account.
     * @param userName  Username to activate
     * @param token     Activation token
     * @return          Response message indicating success or failure
     */
    @PostMapping("/register/activate")
    public ResponseEntity<?> activateUser(@RequestParam(value = "uname") String userName, @RequestParam("token") String token) {
        SignupRequest signupRequest = signupService.findByUserName(userName);

        if (signupRequest == null) {
            return new ResponseEntity<>("Invalid username", HttpStatus.BAD_REQUEST);
        }

        if (!signupRequest.getActivationToken().equals(token)) {

            return new ResponseEntity<>("Invalid Token", HttpStatus.CONFLICT);
        }

        LocalDateTime expiryTime = signupRequest.getValidTill();

        if (expiryTime.isBefore(LocalDateTime.now())) {
            signupService.deleteSignupRequest(signupRequest);
            return new ResponseEntity<>("Token Expired", HttpStatus.CONFLICT);
        }


        User newUser = userService.activateUser(signupRequest);

        if (newUser==null || newUser.getId() == null) {
            signupService.deleteSignupRequest(signupRequest);
            return new ResponseEntity<>("Activation Unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        signupService.deleteSignupRequest(signupRequest);
        return new ResponseEntity<>("Activation Successful \n You may login with this account ", HttpStatus.OK);

    }


    /**
     * Creates registration request
     * Email is sent to provided email address to activate the account
     * @param multipartFile     Profile image file
     * @param profileDetailsDTO Profile details
     * @return                  SignupRequest if successful, or error message
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam(value = "file", required = false) MultipartFile multipartFile, @ModelAttribute("signup") ProfileDetailsDTO profileDetailsDTO) {

        System.out.println("registering "+profileDetailsDTO);

        if (!profileDetailsDTO.getPassword().equals(profileDetailsDTO.getPasswordConfirm())) {
            return new ResponseEntity<>("Passwords dont match", HttpStatus.CONFLICT);
        }

        if (!signupService.validEmailAddress(profileDetailsDTO.getEmail())) {
            return new ResponseEntity<>("Invalid email address", HttpStatus.BAD_REQUEST);
        }

        if (userService.emailExists(profileDetailsDTO.getEmail()) || signupService.emailSubmittedForRegistration(profileDetailsDTO.getEmail())) {
            return new ResponseEntity<>("Email Unavailable", HttpStatus.CONFLICT);
        }

        if (userService.usernameExists(profileDetailsDTO.getUserName()) || signupService.userNameSubmiitedForRegistration(profileDetailsDTO.getUserName())) {
            return new ResponseEntity<>("Username Unavailable", HttpStatus.CONFLICT);
        }


        SignupRequest signupRequest = signupService.createSignupRequest(profileDetailsDTO, multipartFile);

        if (signupRequest == null) {
            return new ResponseEntity<>("Internal Server Error \n Please Try Later", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String message = signupService.mailUser(signupRequest);

        if (message.equals(CoreApplicationConstants.SUCCESS_MESSAGE)) {
            return new ResponseEntity<>(signupRequest, HttpStatus.CREATED);
        } else {
            signupService.deleteSignupRequest(signupRequest);
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Returns the registration page.
     * @return  ModelAndView for the registration page
     */
    @GetMapping("/register/page")
    public ModelAndView registerPage() {

        ModelAndView modelAndView = new ModelAndView("registration");
        modelAndView.addObject("signup", new ProfileDetailsDTO());
        return modelAndView;
    }






}
