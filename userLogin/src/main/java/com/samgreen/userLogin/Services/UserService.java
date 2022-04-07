package com.samgreen.userLogin.Services;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.samgreen.userLogin.Models.LoginUser;
import com.samgreen.userLogin.Models.User;
import com.samgreen.userLogin.Repositories.UserRepository;
    
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
    public User register(User newUser, BindingResult result) {
        
    	Optional<User> optionalUser = userRepo.findByEmail(newUser.getEmail());
    	

    	if(optionalUser.isPresent()) {
    		result.rejectValue("email", "Matches", "An account with that email already exists!");
    	}
    	
       
    	if(!newUser.getPassword().equals(newUser.getConfirm())) {
    		result.rejectValue("confirm", "Matches", "The Confirm Password must match Password!");
    	}
    	
    
    	if(result.hasErrors()) {
    		return null;
    	}
    
       
    	String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
    	newUser.setPassword(hashed);
    	return userRepo.save(newUser);
    	
    }

    public User findById(Long id) {
    	Optional<User> optionalUser = userRepo.findById(id);
    	if(optionalUser.isPresent()) {
    		return optionalUser.get();
    	}
    	return null;
    }

    public UserRepository getUserRepo() {
		return userRepo;
	}

	public void setUserRepo(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	public User login(LoginUser newLogin, BindingResult result) {
    	
    	Optional<User> optionalUser = userRepo.findByEmail(newLogin.getEmail());
  
    	if(!optionalUser.isPresent()) {
    		result.rejectValue("email", "Matches", "User not found!");
    		return null;
    	}
    	
    	User user = optionalUser.get();
    	
    	if(!BCrypt.checkpw(newLogin.getPassword(), user.getPassword())) {
    		result.rejectValue("password", "Matches", "Invalid Password!");
    	}
    	
    	
        
 
    	if(result.hasErrors()) {
    		return null;
    	}
    	
    	return user;
    }
}
    	
    
		

