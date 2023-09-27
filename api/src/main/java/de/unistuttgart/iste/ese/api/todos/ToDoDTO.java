package de.unistuttgart.iste.ese.api.todos;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * The DTO class for a todo
 * This class is necessary because the structure of todos received from the frontend is different
 * to the structure of todos in the backend. Therefore this class is used to parse the data received from
 * the frontend to an object of this class and then use it to create an object of the todo entity class
 */
public class ToDoDTO {
    
    @Size(min = 1)
    @NotBlank
    private String title;
    
    private String description;
    
    private boolean finished;
    
    private List<Long> assigneeIdList;
    
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Date dueDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public List<Long> getAssigneeIdList() {
        return assigneeIdList;
    }

    public void setAssigneeIdList(List<Long> assigneeIdList) {
        this.assigneeIdList = assigneeIdList;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
