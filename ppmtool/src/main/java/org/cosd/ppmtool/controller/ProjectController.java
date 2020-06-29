package org.cosd.ppmtool.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.cosd.ppmtool.entity.Project;
import org.cosd.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project")
@CrossOrigin(origins="*", allowedHeaders="*")
public class ProjectController {

  @Autowired	
  private ProjectService projectService;
  
  @PostMapping("/createProject")
  public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult bindingResult, Principal principal){
	  
	  if(bindingResult.hasErrors()){
		  Map<String,String> errorMap = new HashMap<>();
		  for(FieldError error: bindingResult.getFieldErrors()){
			  errorMap.put(error.getField(), error.getDefaultMessage());
		  }
		  return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
	  }
	   projectService.saveOrUpdate(project, principal.getName());
	   return new ResponseEntity<Project>(project, HttpStatus.CREATED);
  }
  
  @PutMapping("/updateProject/{projectId}")
  public ResponseEntity<?> updateProject(@PathVariable String projectId, @RequestBody Project project, Principal principal,
		  BindingResult bindingResult){
	  
	  if(bindingResult.hasErrors()){
		  Map<String,String> errorMap = new HashMap<>();
		  for(FieldError error: bindingResult.getFieldErrors()){
			  errorMap.put(error.getField(), error.getDefaultMessage());
		  }
		  return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
	  }
	   Project projectUpdate = projectService.findProjectByIdentifier(projectId, principal.getName());
	   projectService.saveOrUpdate(projectUpdate, principal.getName());
	   return new ResponseEntity<Project>(project, HttpStatus.CREATED);
  }
  
  
  
  @GetMapping("/getProjectById/{projectId}")
  public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal){
	  
	  Project project = projectService.findProjectByIdentifier(projectId, principal.getName());
	  return new ResponseEntity<Project>(project, HttpStatus.OK);
  }
  
  @GetMapping("/getProjects")
  public ResponseEntity<Iterable<Project>> getProjects(Principal principal){
	  
	  return new ResponseEntity<Iterable<Project>>(projectService.findAllProjects(principal.getName()), HttpStatus.OK);
  }
  
  @DeleteMapping("/deleteProject/{projectId}")
  public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal){
	  
	  projectService.deleteProjectByIdentifier(projectId, principal.getName());
	  return new ResponseEntity<String>("Project with " + projectId + " deleted", HttpStatus.OK);
  }
	
}
