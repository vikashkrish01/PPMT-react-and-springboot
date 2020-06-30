package org.cosd.ppmtool.controller;

import org.cosd.ppmtool.entity.User;
import org.cosd.ppmtool.payload.JWTLoginSuccessResponse;
import org.cosd.ppmtool.payload.LoginRequest;
import org.cosd.ppmtool.security.JwtTokenProvider;
import org.cosd.ppmtool.services.UserService;
import org.cosd.ppmtool.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import static org.cosd.ppmtool.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins="*", allowedHeaders="*")
public class UserController {


    @Autowired
    private UserService userService;
    
    @Autowired
    private UserValidator userValidator;
    
    
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;



    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        
    	if(result.hasErrors()){
    		  Map<String,String> errorMap = new HashMap<>();
    		  for(FieldError error: result.getFieldErrors()){
    			  errorMap.put(error.getField(), error.getDefaultMessage());
    		  }
    		  return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
    	  }
    	
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX +  tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){
        // Validate passwords match

    	userValidator.validate(user, result);
    	
    	if(result.hasErrors()){
  		  Map<String,String> errorMap = new HashMap<>();
  		  for(FieldError error: result.getFieldErrors()){
  			  errorMap.put(error.getField(), error.getDefaultMessage());
  		  }
  		  return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
  	  }

        User newUser = userService.saveUser(user);

        return  new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }
}