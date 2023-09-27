package de.unistuttgart.iste.ese.api.todos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.unistuttgart.iste.ese.api.assignees.Assignee;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * The entity class for a todo
 */
@Entity
@Table(name = "todos")
public class ToDo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Size(min = 1)
    @NotBlank
    private String title;
    
    private String description;
    
    @ManyToMany(targetEntity = de.unistuttgart.iste.ese.api.assignees.Assignee.class, fetch = FetchType.EAGER)
    @JsonProperty("assigneeList")
    private Set<Assignee> assigneeList;
    private boolean finished;
    
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date dueDate;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date finishedDate;
    
    public ToDo(){}
    
    public ToDo(String title, String description, Set<Assignee> assigneeList, boolean finished, Date createdDate, Date dueDate){
        this.title = title;
        this.description = description;
        this.assigneeList = assigneeList;
        this.finished = finished;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
    }

    public Set<Assignee> getAssignees() {
        return assigneeList;
    }

    public void setAssignees(Set<Assignee> assignees) {
        this.assigneeList = assignees;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }
}
