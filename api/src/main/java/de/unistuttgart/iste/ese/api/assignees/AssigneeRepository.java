package de.unistuttgart.iste.ese.api.assignees;

import org.springframework.data.repository.CrudRepository;
import java.util.Set;

/**
 * The CRUD repository for the assignees
 */
public interface AssigneeRepository extends CrudRepository<Assignee, Long> {
    
    Assignee findById(long id);
    
    Set<Assignee> findAll();
}
