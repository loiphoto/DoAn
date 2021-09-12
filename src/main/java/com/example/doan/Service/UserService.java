package com.example.doan.Service;

import com.example.doan.Entity.User;
import com.example.doan.Repository.RoleRepository;
import com.example.doan.Repository.UserRepository;
import com.example.doan.exception.UserAlreadyExistException;
import com.example.doan.exception.UserNotFoundException;
import com.example.doan.model.CustomUserDetails;
import com.example.doan.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void save(User user){
        userRepository.save(user);
    }

    public User getUserById(Long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        throw new UserNotFoundException("Could not find any users with ID" + id);
    }

    public void deleteById(Long id) throws UserNotFoundException {
        Long count = userRepository.countById(id);
        if(count == null || count == 0){
            throw new UserNotFoundException("Could not find any users with ID" + id);
        }
        userRepository.deleteById(id);
    }

    public User getCurrentlyLoggedInUser(Authentication authentication){
        if(authentication == null) return null;

        User user = null;
        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails){
            String email = ((CustomUserDetails) principal).getUsername();
            user = userRepository.findByEmail(email);
        }
        return user;
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public void registerNewUserAccount(UserLogin userLogin) throws UserAlreadyExistException {
        if (emailExists(userLogin.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + userLogin.getEmail());
        }

        User user = new User();
        user.setEmail(userLogin.getEmail());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(userLogin.getPassword());
        user.setPassword(encodedPassword);
        user.setPhone("");
        user.setRole(roleRepository.getById(2l));

        System.out.println(user);
        userRepository.save(user);
    }
}
