package de.unistuttgart.iste.ese.api.todos;

import de.unistuttgart.iste.ese.api.assignees.Assignee;
import de.unistuttgart.iste.ese.api.assignees.AssigneeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Iterator;
import java.util.Set;

/**
 * A Service class for the todos
 */
@Service
public class ToDoService {
    
    @Autowired
    private ToDoRepository toDoRepository;
    
    @Autowired
    private AssigneeService assigneeService;
    
    public Set<ToDo> getAllTodos(){
        return toDoRepository.findAll();
    }
    
    public ToDo getTodoById(long id){
        return toDoRepository.findById(id);
    }
    
    public ToDo saveTodo(ToDo toDo){
        return toDoRepository.save(toDo);
    }
    
    public void deleteTodo(ToDo toDo){
        toDoRepository.delete(toDo);
    }

    /**
     * This method deletes an assignee from all todos which he is assigned to
     * 
     * @param id The id of the assignee which should be deleted from all todos
     */
    public void deleteAssigneeFromTodos(long id){
        Assignee assigneeToDelete = assigneeService.getAssigneeById(id);
        Set<ToDo> allTodos = this.getAllTodos();
        for (ToDo toDo : allTodos){
            for (Iterator<Assignee> i = toDo.getAssignees().iterator(); i.hasNext();){
                if (i.next().equals(assigneeToDelete)){
                    i.remove();
                }
            }
            this.saveTodo(toDo);
        }
    }
    
}
