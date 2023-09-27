package de.unistuttgart.iste.ese.api.assignees;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Service class for the assignees
 */
@Service
public class AssigneeService {
    
    @Autowired
    private AssigneeRepository assigneeRepository;
    
    public Set<Assignee> getAllAssignees(){
        return assigneeRepository.findAll();
    }
    
    public Assignee getAssigneeById(long id){
        return assigneeRepository.findById(id);
    }
    
    public Assignee saveAssignee(Assignee assignee){
        return assigneeRepository.save(assignee);
    }
    
    public void deleteAssignee(Assignee assignee){
        assigneeRepository.delete(assignee);
    }

    /**
     * This method searches all assignees for a given list of ids and returns them.
     * It is required because the api expects a list of ids of assignees but in the backend
     * the related assignees should be stored. 
     *
     * @param ids The list of ids from assignees which should be saved
     * @return A set with all assignees with an id contained in ids
     * @throws ResponseStatusException, which is thrown if the given id list is null, the list contains an id for which no assignee exists or if the list contains an id twice
     */
    public Set<Assignee> getAssigneesFromIDs(List<Long> ids){
        Set<Assignee> assigneesToSave = new HashSet<>();
        //flag variable to check for doubled ids and throw an exception if ids are doubled
        boolean noDoubleAssignees = true;
        if (ids == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assignees can not be null!");
        }
        for (Long id : ids){
            Assignee assignee = this.getAssigneeById(id);
            if (assignee == null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Related Assignee with ID %s not found!", id));
            }
            if (noDoubleAssignees){
                noDoubleAssignees = assigneesToSave.add(assignee);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Related Assignee with ID %s id already assigned!", id));
            }
        }
        return assigneesToSave;
    }
    
}
