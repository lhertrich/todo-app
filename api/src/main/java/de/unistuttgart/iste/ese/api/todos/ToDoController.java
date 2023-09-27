package de.unistuttgart.iste.ese.api.todos;

import de.unistuttgart.iste.ese.api.assignees.Assignee;
import de.unistuttgart.iste.ese.api.assignees.AssigneeService;
import de.unistuttgart.iste.ese.api.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

/**
 * The REST Controller class for the todos
 * It contains all necessary methods for a CRUD application
 */
@RestController
public class ToDoController {
    
    @Autowired
    private ToDoService toDoService;
    
    @Autowired
    private AssigneeService assigneeService;
    
    @Autowired
    private MailService mailService;
    
    @GetMapping("/todos")
    public Set<ToDo> getAllTodos(){
        return toDoService.getAllTodos();
    }
    
    @GetMapping("/todos/{id}")
    public ToDo getTodo(@PathVariable("id") long id){
        ToDo searchedTodo = toDoService.getTodoById(id);
        if (searchedTodo != null){
            return searchedTodo;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Todo with ID %s not found!", id));
    }
    
    @PostMapping("/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public ToDo createTodo(@Valid @RequestBody ToDoDTO requestBody){
        
        Set<Assignee> assigneesToSave = new HashSet<>();
        if (requestBody.getAssigneeIdList()!=null){
            assigneesToSave = assigneeService.getAssigneesFromIDs(requestBody.getAssigneeIdList());
        }
        
        ToDo toDoToSave = new ToDo(requestBody.getTitle(), requestBody.getDescription(), assigneesToSave, requestBody.isFinished(), new Date(System.currentTimeMillis()), requestBody.getDueDate());
        
        if(requestBody.isFinished()){
            toDoToSave.setFinishedDate(new Date(System.currentTimeMillis()));
        }
        
        for (Assignee assignee : assigneesToSave){
            this.mailService.sendMail(assignee, toDoToSave);
        }
        
        return toDoService.saveTodo(toDoToSave);
    }
    
    @PutMapping("/todos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ToDo changeTodo(@PathVariable("id") long id, @Valid @RequestBody ToDoDTO requestBody){
        ToDo toDoToUpdate = toDoService.getTodoById(id);
        Set<Assignee> assigneesToSave;
        //save the old assignees to determine which assignees have to be informed per mail
        Set<Assignee> oldAssignees;
        if (toDoToUpdate != null){
            oldAssignees = toDoToUpdate.getAssignees();
            toDoToUpdate.setTitle(requestBody.getTitle());
            toDoToUpdate.setDescription(requestBody.getDescription());
            assigneesToSave = assigneeService.getAssigneesFromIDs(requestBody.getAssigneeIdList());
            toDoToUpdate.setAssignees(assigneesToSave);
            toDoToUpdate.setFinished(requestBody.isFinished());
            toDoToUpdate.setDueDate(requestBody.getDueDate());
            if (requestBody.isFinished()){
                toDoToUpdate.setFinishedDate(new Date(System.currentTimeMillis()));
            } else {
                toDoToUpdate.setFinishedDate(null);
            }
            this.mailAllNewAssignees(oldAssignees, toDoToUpdate);
            return toDoService.saveTodo(toDoToUpdate);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Todo with ID %s not found!", id));
    }
    
    @DeleteMapping("/todos/{id}")
    public ToDo deleteTodo(@PathVariable("id") long id){
        ToDo toDoToDelete = toDoService.getTodoById(id);
        if (toDoToDelete != null){
            toDoService.deleteTodo(toDoToDelete);
            return toDoToDelete;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Todo with ID %s not found!", id));
    }

    /*
     * This method determines which assignees are new given a set of old assignees and a new todo
     * and sends a mail to every new assignee with the mail service
     * 
     * @param oldAssignees The set of old assignees
     * @param newTodo The updated todo which contains also an updated set of assignees
     */
    private void mailAllNewAssignees(final Set<Assignee> oldAssignees, final ToDo newTodo){
        Set<Assignee> newAssignees = new HashSet<>(newTodo.getAssignees());
        newAssignees.removeAll(oldAssignees);
        for (Assignee assignee : newAssignees){
           this.mailService.sendMail(assignee, newTodo);
        }
    }
    
}
