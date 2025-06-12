package com.example.vidupcoremodule.core.service.userservices;



import com.example.vidupcoremodule.CoreApplicationConstants;
import com.example.vidupcoremodule.core.View_DTO.ProfileDetailsDTO;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Role;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.SignupRequest;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.LoggedInUserDetails;
import com.example.vidupcoremodule.core.repository.RoleRepository;
import com.example.vidupcoremodule.core.repository.SignupRequestRepository;
import com.example.vidupcoremodule.core.repository.UserRepository;
import com.example.vidupcoremodule.core.util.UtilClass;
import com.example.vidupcoremodule.storage.LocalStorageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepo;

    @Autowired
    UtilClass utilClass;

    @Autowired
    SignupRequestRepository signupRequestRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    LocalStorageService storageService;
    public UserService(LocalStorageService storageService) {
        this.storageService = storageService;
    }



    @Bean
    private User guestUser()
    {
        User user = new User();
        user.setUserName("guest");
        user.setId(-1);
        return user;
    }

    public Boolean userIdExists(String userId) {

        User user = userRepo.findUserById(Integer.parseInt(userId));
        return user != null;
    }

    public Boolean usernameExists(String username) {
        return userRepo.findByUserName(username) != null;
    }



    public Boolean emailExists(String email) {
        User user = userRepo.findByEmail(email);
        return user != null;
    }

    public User getUserById(String userId) {
        User user = userRepo.findUserById(Integer.parseInt(userId));
        return user;
    }



    public User getGuestUser()
    {
        return guestUser();
    }


    public User activateUser(SignupRequest signupRequest)
    {
        User newUser = new User();

        newUser.setFName(signupRequest.getFName());
        newUser.setLName(signupRequest.getLName());
        newUser.setEmail(signupRequest.getEmail());
        newUser.setAge(signupRequest.getAge());
        newUser.setSex(signupRequest.getSex());
        newUser.setContact(signupRequest.getContact());
        newUser.setUserName(signupRequest.getUserName());
        newUser.setPassword(signupRequest.getPassword());
        newUser.setProfileImage(signupRequest.getProfileImage());

        Set<Role> basicRole = Collections.singleton(roleRepository.findByRoleName("USER"));
        newUser.setRoles(basicRole);

        return userRepo.save(newUser);
    }

    public User findByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }



    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepo.findByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        UserDetails toReturn = new LoggedInUserDetails(user);

        System.out.println(toReturn.getUsername()+" "+toReturn.getAuthorities());
        return toReturn;
    }

    public UserDetails loadUserByEmail(String email)
            throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        UserDetails toReturn = new LoggedInUserDetails(user);

        System.out.println(toReturn.getUsername()+" "+toReturn.getAuthorities());
        return toReturn;
    }

    public byte[] getProfileImage(Integer userId)
    {
        return userRepo.findUserById(userId).getProfileImage();
    }

    public byte[] getProfileImage(String userName)
    {
        User user = userRepo.findByUserName(userName);
        return getProfileImage(user.getId());

    }

    @Transactional
    public User updateUser(ProfileDetailsDTO profileDetailsDTO, MultipartFile multipartFile, User toUpdate) throws IOException
    {

        if(!multipartFile.isEmpty())
            toUpdate.setProfileImage(multipartFile.getBytes());

        if(profileDetailsDTO.getEmail()!=null && !profileDetailsDTO.getEmail().isEmpty())
            toUpdate.setEmail(profileDetailsDTO.getEmail());

        if(profileDetailsDTO.getFName()!=null && !profileDetailsDTO.getFName().isEmpty())
           toUpdate.setFName(profileDetailsDTO.getFName());

        if(profileDetailsDTO.getLName()!=null && !profileDetailsDTO.getLName().isEmpty())
            toUpdate.setLName(profileDetailsDTO.getLName());

        if(profileDetailsDTO.getContact()!=null && !profileDetailsDTO.getContact().isEmpty())
            toUpdate.setContact(profileDetailsDTO.getContact());

        if(profileDetailsDTO.getPassword()!=null && !profileDetailsDTO.getPassword().isEmpty())
            toUpdate.setPassword(passwordEncoder.encode(profileDetailsDTO.getPassword()));

        toUpdate = userRepo.save(toUpdate);
        System.out.println(toUpdate);
        return toUpdate;
    }


    public User updateUser(MultipartFile multipartFile,  String email,
                          String fname, String lname,
                           String contact,
                           String password,
                           String passwordConfirm,User currentUser) throws IOException {
        if(!validInput(password,passwordConfirm))
            return null;



        if(!multipartFile.isEmpty())
            currentUser.setProfileImage(multipartFile.getBytes());

        if(email!=null && !email.isEmpty())
            currentUser.setEmail(email);

        if(fname!=null && !fname.isEmpty())
            currentUser.setFName(fname);

        if(lname!=null && !lname.isEmpty())
            currentUser.setLName(lname);

        if(contact!=null && !contact.isEmpty())
            currentUser.setContact(contact);

        if(!password.isEmpty())
            currentUser.setPassword(passwordEncoder.encode(password));

        currentUser = userRepo.save(currentUser);
        System.out.println(currentUser);
        return currentUser;

    }

    private boolean validInput(
                               String password,
                               String passwordConfirm)
    {
        return password.equals(passwordConfirm);
    }

    public User registerUser( String userName,String email, String password,String passwordConfirm,MultipartFile multipartFile,String age,String gender)
    {

        if(userName==null || userName.isEmpty() || email==null || email.isEmpty() || password==null
                || password.isEmpty() || passwordConfirm==null || passwordConfirm.isEmpty() || age==null || age.isEmpty() || gender==null || gender.isEmpty())
            return null;

        if(userRepo.findByUserName(userName)!=null)
            return null;

        if(!password.equals(passwordConfirm))
            return null;

        User newUser = new User();

        newUser.setUserName(userName);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setEmail(email);

        newUser.setAge(Integer.parseInt(age));
        newUser.setSex(gender);

        try {
            byte[] profileImage = (multipartFile == null || multipartFile.isEmpty()) ? storageService.readFileAsBytesFromClassPath(CoreApplicationConstants.blankUserImage)
                    : multipartFile.getBytes();

            newUser.setProfileImage(profileImage);
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
            return null;
        }



        return userRepo.save(newUser);



    }




}