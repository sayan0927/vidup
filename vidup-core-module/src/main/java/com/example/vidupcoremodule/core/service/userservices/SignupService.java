package com.example.vidupcoremodule.core.service.userservices;


import com.example.vidupcoremodule.CoreApplicationConstants;
import com.example.vidupcoremodule.core.View_DTO.ProfileDetailsDTO;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.SignupRequest;
import com.example.vidupcoremodule.core.repository.SignupRequestRepository;
import com.example.vidupcoremodule.core.util.Mailer;
import com.example.vidupcoremodule.core.util.UtilClass;
import com.example.vidupcoremodule.storage.LocalStorageService;
import jakarta.transaction.Transactional;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@Service
public class SignupService {


    private static final Logger logger = LoggerFactory.getLogger(SignupService.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    LocalStorageService storageService;

    @Autowired
    SignupRequestRepository signupRequestRepository;

    @Autowired
    Mailer mailer;

    @Autowired
    UtilClass utilClass;


    public SignupRequest createSignupRequest(ProfileDetailsDTO dto, MultipartFile multipartFile)
    {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(dto.getEmail());
        signupRequest.setFName(dto.getFName());
        signupRequest.setLName(dto.getLName());
        signupRequest.setAge(dto.getAge());
        signupRequest.setSex(dto.getSex());
        signupRequest.setContact(dto.getContact());
        signupRequest.setPassword(passwordEncoder.encode(dto.getPassword()));
        signupRequest.setActivationToken(getActivationToken(dto));
        signupRequest.setUserName(dto.getUserName());


        byte[] profileImg;

        try {
            profileImg = multipartFile.isEmpty() ? storageService.readFileAsBytesFromClassPath(CoreApplicationConstants.blankUserImage) : multipartFile.getBytes();
            signupRequest.setProfileImage(profileImg);
        } catch (IOException ioException) {
            System.out.println("err");
            return null;
        }

        return signupRequestRepository.save(signupRequest);
    }

    public void deleteSignupRequest(SignupRequest signupRequest) {
        signupRequestRepository.delete(signupRequest);
    }

    public String mailUser(SignupRequest signupRequest) {
        String recipient = signupRequest.getEmail();
        String emailMessage = craftEmailMessage(signupRequest);

        if(emailMessage==null)
            return null;
        try {
            mailer.sendEmail(recipient, "Vidup Account Activation", craftEmailMessage(signupRequest));
            return CoreApplicationConstants.SUCCESS_MESSAGE;
        }
        catch (MailException mailException) {
            return "Failed";
        }

    }

    @Transactional
    public void clearExpiredSignupRequests()
    {
        signupRequestRepository.deleteAllByValidTillBefore(LocalDateTime.now());
    }

    private String craftEmailMessage(SignupRequest signupRequest) {
        String userName = signupRequest.getUserName();
        String token = signupRequest.getActivationToken();

        try {
            String coreIp = utilClass.getCurrentIp();
            String corePort = utilClass.getCorePort();

            String url = "http://" + coreIp + ":" + corePort + "/users/register/activate/page?uname=" + userName + "&token=" + token;
            return url;
        }
        catch (UnknownHostException unknownHostException) {

            System.out.println(unknownHostException.getMessage());
            return null;
        }

    }

    String getActivationToken(ProfileDetailsDTO profileDetailsDTO) {
        return passwordEncoder.encode(profileDetailsDTO.getUserName());
    }

    public Boolean validEmailAddress(String email)
    {
        return EmailValidator.getInstance().isValid(email);
    }

    public SignupRequest findByUserName(String userName)
    {
        return signupRequestRepository.findByUserName(userName);
    }

    /**
     * Checks if username was submitted during registration of new user, registration not yet complete
     * @param username  The username
     * @return          True if currently incomplete registration request exist with this username , False otherwise
     */
    public Boolean userNameSubmiitedForRegistration(String username) {
        SignupRequest signupRequest =  signupRequestRepository.findByUserName(username);
        System.out.println(signupRequest+" uname check");
        return signupRequest != null;
    }
    /**
     * Checks if email was submitted during registration of new user, registration not yet complete
     * @param email     The email
     * @return          True if currently incomplete registration request exist with this email , False otherwise
     */
    public Boolean emailSubmittedForRegistration(String email) {
        SignupRequest signupRequest = signupRequestRepository.findByEmail(email);
        System.out.println(signupRequest+" email check");
        return signupRequest != null;
    }

    public void deleteRequest(SignupRequest signupRequest)
    {
        signupRequestRepository.delete(signupRequest);
    }
}
