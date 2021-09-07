package io.zhengweiliu.ppmtool.services;

import io.zhengweiliu.ppmtool.domain.Backlog;
import io.zhengweiliu.ppmtool.domain.Project;
import io.zhengweiliu.ppmtool.domain.User;
import io.zhengweiliu.ppmtool.exceptions.ProjectIdException;
import io.zhengweiliu.ppmtool.exceptions.ProjectNotFoundException;
import io.zhengweiliu.ppmtool.repositories.BacklogRepository;
import io.zhengweiliu.ppmtool.repositories.ProjectRepository;
import io.zhengweiliu.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project,String username){
        try{
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            if(project.getId()==null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
            else{
                Project existingProject= findProjectByIdentfier(project.getProjectIdentifier(),username);
                if(existingProject.getId()!=project.getId()) throw  new ProjectIdException("The database ID of Project ID '" + project.getProjectIdentifier().toUpperCase() + "' doesn't match.");
                project.setBacklog(backlogRepository.findByProjectIdentifier((project.getProjectIdentifier()).toUpperCase()));
            }

            return projectRepository.save(project);
        }
        catch(ProjectIdException | ProjectNotFoundException e){
            throw e;
        }
        catch (Exception e){
            throw  new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
        }

        //return projectRepository.save(project);
    }

    public Project findProjectByIdentfier(String projectId, String username){

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project==null){
            throw new ProjectIdException("Project ID '" + projectId.toUpperCase() + "' does not exist");
        }

        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project ID '" + projectId.toUpperCase() + "' not found in you account");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username){
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId,String username){
        Project project = findProjectByIdentfier(projectId,username);


        projectRepository.delete(project);
    }


}
