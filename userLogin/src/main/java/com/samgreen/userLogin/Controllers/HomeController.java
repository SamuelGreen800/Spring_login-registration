package com.samgreen.userLogin.Controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.samgreen.userLogin.Models.LoginUser;
import com.samgreen.userLogin.Models.User;
import com.samgreen.userLogin.Services.UserService;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	@GetMapping
	public String index() {
		return "redirect:/login";
	}
	
	@GetMapping("/login")
	public String signIn(Model model) {
	    model.addAttribute("newUser", new User());
	    model.addAttribute("newLogin", new LoginUser());
	    return "login.jsp";
	    
	}
	    
	@GetMapping("/dashboard")
	public String dashboard(HttpSession session, Model model) {
		if(session.getAttribute("userId") == null) {
			return "redirect:/logout";
		}
		
		Long userId = (Long) session.getAttribute("userId");
		model.addAttribute("user", userService.findById(userId));
		
		return "dashboard.jsp";
	}
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("newUser") User newUser, BindingResult result, Model model, HttpSession session) {
	     
		User user = userService.register(newUser, result);
		
	    if(result.hasErrors()) {
	        model.addAttribute("newLogin", new LoginUser());
	        return "login.jsp";
	    }
	    
	    session.setAttribute("userId", user.getId());
	 
	    return "redirect:/dashboard";
	}
	 
	    
	@PostMapping("/auth")
	public String auth(@Valid @ModelAttribute("newLogin") LoginUser newLogin, BindingResult result, Model model, HttpSession session) {
	     
		User user = userService.login(newLogin, result);
	 
	    if(result.hasErrors()) {
	        model.addAttribute("newUser", new User());
	        return "login.jsp";
	    }
	    
	    session.setAttribute("userId", user.getId());
	    return "redirect:/dashboard";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.setAttribute("userId", null);
	     
	    return "redirect:/";
	}
	     
	//==================================================
	
	public UserService getUserService() {
		return userService;
	}
	 
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}





