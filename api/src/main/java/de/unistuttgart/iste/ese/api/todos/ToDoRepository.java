package de.unistuttgart.iste.ese.api.todos;

import org.springframework.data.repository.CrudRepository;
import java.util.Set;

/**
 * The CRUD repository for the todos
 */
public interface ToDoRepository extends CrudRepository<ToDo, Long> {
    ToDo findById(long id);
    
    Set<ToDo> findAll();
}
