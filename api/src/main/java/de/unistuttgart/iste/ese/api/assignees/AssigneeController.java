package de.unistuttgart.iste.ese.api.assignees;

import de.unistuttgart.iste.ese.api.todos.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.util.Set;

/**
 * The REST Controller class for the assignees
 * It contains all necessary methods for a CRUD application
 */
@RestController
public class AssigneeController {
    
    @Autowired
    private AssigneeService assigneeService;
    
    @Autowired
    private ToDoService toDoService;
    
    @GetMapping("/assignees")
    public Set<Assignee> getAllAssignees(){
        return assigneeService.getAllAssignees();
    }
    
    @GetMapping("/assignees/{id}")
    public Assignee getAssignee(@PathVariable("id") long id){
        Assignee searchedAssignee = assigneeService.getAssigneeById(id);
        if(searchedAssignee != null){
            return searchedAssignee;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Assignee with ID %s not found!", id));
    }
    
    @PostMapping("/assignees")
    @ResponseStatus(HttpStatus.CREATED)
    public Assignee createAssignee(@Valid @RequestBody Assignee assignee){
        Assignee assigneeToCreate = new Assignee(assignee.getPrename(), assignee.getName(), assignee.getEmail());
        return assigneeService.saveAssignee(assigneeToCreate);
    }
    
    @PutMapping("/assignees/{id}")
    public Assignee updateAssignee(@PathVariable("id") long id, @Valid @RequestBody Assignee assignee){
        assignee.setId(id);
        Assignee assigneeToUpdate = assigneeService.getAssigneeById(id);
        if(assigneeToUpdate!= null){
            return assigneeService.saveAssignee(assignee);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Assignee with ID %s not found!", id));
    }
    
    @DeleteMapping("/assignees/{id}")
    public Assignee deleteAssigne(@PathVariable("id") long id){
        Assignee assigneeToDelete = assigneeService.getAssigneeById(id);
        if(assigneeToDelete != null){
            toDoService.deleteAssigneeFromTodos(id);
            assigneeService.deleteAssignee(assigneeToDelete);
            return assigneeToDelete;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Assignee with ID %s not found!", id));
    }
        
}
