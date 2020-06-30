package org.cosd.ppmtool.services;

import org.cosd.ppmtool.entity.Backlog;
import org.cosd.ppmtool.entity.Project;
import org.cosd.ppmtool.entity.ProjectTask;
import org.cosd.ppmtool.exceptions.ProjectNotFoundException;
import org.cosd.ppmtool.repository.BacklogRepository;
import org.cosd.ppmtool.repository.ProjectRepository;
import org.cosd.ppmtool.repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import antlr.collections.List;

@Service
public class ProjectTaskService {


    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private ProjectService projectService;


    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){

 
        //PTs to be added to a specific project, project != null, BL exists
        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
        //set the bl to pt
        projectTask.setBacklog(backlog);
        //we want our project sequence to be like this: IDPRO-1  IDPRO-2  ...100 101
        Integer BacklogSequence = backlog.getPTSequence();
        // Update the BL SEQUENCE
        BacklogSequence++;

        backlog.setPTSequence(BacklogSequence);

        //Add Sequence to Project Task
        projectTask.setProjectSequence(backlog.getProjectIdentifier()+"-"+BacklogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        //INITIAL priority when priority null

        //INITIAL status when status is null
        if(projectTask.getStatus()==""|| projectTask.getStatus()==null){
            projectTask.setStatus("TO_DO");
        }

        if(projectTask.getPriority()==null || projectTask.getPriority() == 0){ //In the future we need projectTask.getPriority()== 0 to handle the form
            projectTask.setPriority(3);
        }

        return projectTaskRepository.save(projectTask);
 
    }
    
    public Iterable<ProjectTask> findByBacklogId(String id, String username){
    	
    	projectService.findProjectByIdentifier(id, username);
    	
    	return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }
    
    public ProjectTask findPTByProjectSequence(String backlogId,String sequence, String username){
    	
    	projectService.findProjectByIdentifier(backlogId, username);
    	
    	ProjectTask projectTask = projectTaskRepository.findByProjectSequence(sequence);
    	if(projectTask == null){
    		throw new ProjectNotFoundException("Project Task with "+ sequence+ " not found");
    	}
    	if(!projectTask.getProjectIdentifier().equals(backlogId)){
    		throw new ProjectNotFoundException("Project task " + sequence + " does not exist in "+ backlogId);
    	}
    	return projectTask;
    }
    
    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id, username);

        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }
    
    public void deletePTByProjectSequence(String backlog_id, String pt_id, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        projectTaskRepository.delete(projectTask);
    } 
    
    
}