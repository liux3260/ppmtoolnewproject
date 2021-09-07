package io.zhengweiliu.ppmtool.services;

import io.zhengweiliu.ppmtool.domain.Backlog;
import io.zhengweiliu.ppmtool.domain.Project;
import io.zhengweiliu.ppmtool.domain.ProjectTask;
import io.zhengweiliu.ppmtool.exceptions.ProjectNotFoundException;
import io.zhengweiliu.ppmtool.repositories.BacklogRepository;
import io.zhengweiliu.ppmtool.repositories.ProjectRepository;
import io.zhengweiliu.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask,String username){
        //PTs to be added to a specific project, project != null, BL exists
        //try{
            Backlog backlog = projectService.findProjectByIdentfier(projectIdentifier,username).getBacklog();
                    //backlogRepository.findByProjectIdentifier((projectIdentifier));

            //set the bl to pt
            projectTask.setBacklog(backlog);
            //we want our project sequence to be like this: TEST-1 TEST-2 ...100 101
            Integer BacklogSequence = backlog.getPTSequence();
            //Update the BL SEQUENCE
            BacklogSequence++;
            backlog.setPTSequence(BacklogSequence);
            //Add BL SEQUENCE to project task
            projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            //INITIAL priority when priority null
            if(projectTask.getPriority()==null || projectTask.getPriority()==0){
                projectTask.setPriority(3);
            }
            //INITIAL status when status is null
            if(projectTask.getStatus()==null || projectTask.getStatus()==""){
                projectTask.setStatus("TO_DO");
            }
            return projectTaskRepository.save(projectTask);
        //}
        //catch(Exception e){
            //Exceptions: Project not found
            //throw new ProjectNotFoundException("Project not found.");
        //}


    }

    public Iterable<ProjectTask> findBacklogById(String backlog_id,String username) {
        projectService.findProjectByIdentfier(backlog_id,username);
                //projectRepository.findByProjectIdentifier(backlog_id);
        //if(project ==null){
            //throw new ProjectNotFoundException("Project ID '" + backlog_id.toUpperCase() + "' does not exist");
        //}

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);

    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id,String username){
        //make sure we are searching on an existing backlog
        //Backlog backlog =
        projectService.findProjectByIdentfier(backlog_id,username).getBacklog();
        //if(backlog==null){
            //throw new ProjectNotFoundException("Project ID '" + backlog_id.toUpperCase() + "' does not exist");
        //}
        //make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if(projectTask ==null){
            throw new ProjectNotFoundException("Project task '" + pt_id + "' does not exist");
        }

        //make sure that the backlog/project id in the path corresponds ti the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project task '" + pt_id + "' does not exist in project ID '" +backlog_id.toUpperCase() + "'");
        }


        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask,String backlog_id,String pt_id,String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id,username);


        projectTask = updatedTask;
        return projectTaskRepository.save(projectTask);


    }

    public void deletePTByProjectSequence(String backlog_id,String pt_id,String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id,username);
        projectTaskRepository.delete(projectTask);

    }
}
