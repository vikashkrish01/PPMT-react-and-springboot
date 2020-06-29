package org.cosd.ppmtool.controller;


import org.cosd.ppmtool.entity.ProjectTask;
import org.cosd.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;



    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask,
                                            BindingResult result, @PathVariable String backlog_id, Principal principal){

 
        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id, projectTask, principal.getName());

        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);

    }
    
    @GetMapping("/getList/{backlog_id}")
    public Iterable<ProjectTask> getListProjectTask(@PathVariable String backlog_id, Principal principal){
    	return projectTaskService.findByBacklogId(backlog_id, principal.getName());
    }

    @GetMapping("/getProjectTask/{backlog_id}/{pt_id}")
    public ResponseEntity<?> getPTByProjectSwquence(@PathVariable String backlog_id, String pt_id, Principal principal){
    	ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlog_id, pt_id, principal.getName());
    	return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }
    
    @PatchMapping("updatePT/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
                                               @PathVariable String backlog_id, @PathVariable String pt_id , Principal principal){

    	if(result.hasErrors()){
  		  Map<String,String> errorMap = new HashMap<>();
  		  for(FieldError error: result.getFieldErrors()){
  			  errorMap.put(error.getField(), error.getDefaultMessage());
  		  }
  		  return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
  	  }

        ProjectTask updatedTask = projectTaskService.updateByProjectSequence(projectTask,backlog_id,pt_id, principal.getName());

        return new ResponseEntity<ProjectTask>(updatedTask,HttpStatus.OK);

    }
    
    @DeleteMapping("deletePT/{backlog_id}/{pt_id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal){
        projectTaskService.deletePTByProjectSequence(backlog_id, pt_id, principal.getName());

        return new ResponseEntity<String>("Project Task "+pt_id+" was deleted successfully", HttpStatus.OK);
    }

}