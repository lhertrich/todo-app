package de.unistuttgart.iste.ese.api.csv;

import de.unistuttgart.iste.ese.api.assignees.Assignee;
import de.unistuttgart.iste.ese.api.todos.ToDo;
import de.unistuttgart.iste.ese.api.todos.ToDoService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * The Service class for the csv export
 */
@Service
public class CSVService {
    
   @Autowired 
    private ToDoService toDoService;

    /**
     * This method creates a CSVPrinter to print all given todos in a csv file
     * 
     * @param writer The writer needed to initialize the CSVPrinter
     */
   public void writeTodosToCSV(Writer writer) {
       Set<ToDo> toDos = toDoService.getAllTodos();
       String[] HEADERS = {"id", "title", "description", "finished", "assignees", "createdDate", "dueDate", "finishedDate"};
       try (CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(HEADERS))) {
           for (ToDo toDo : toDos){
               printer.printRecord(toDo.getId(), toDo.getTitle(), toDo.getDescription(), toDo.isFinished(), this.getAssigneeString(toDo.getAssignees()), this.getDateString(toDo.getCreatedDate()), this.getDateString(toDo.getDueDate()), this.getDateString(toDo.getFinishedDate()));
           }
       } catch (IOException e){
           e.printStackTrace();
       }
   }

    /**
     * This method creates a string of the names of a given set of assignees with the following form:
     * prename1 name1+prename2 name2+...+prenameN nameN
     * 
     * @param assignees The set of assignees which names should be added to the string
     * @return assigneeString a String with all prenames and names of the assignees in the given set in the described form
     */
   public String getAssigneeString(Set<Assignee> assignees){
       String assigneeString = "";
       Iterator<Assignee> iterator = assignees.iterator();
       while (iterator.hasNext()){
           Assignee currentAssignee = iterator.next();
           assigneeString += currentAssignee.getPrename()+" "+currentAssignee.getName();
           if (iterator.hasNext()){
               assigneeString += "+";
           }
       }
       return assigneeString;
   }

    /*
     * A date formatter which converts a given java.util.date into an string with the form yyyy-MM-dd
     * 
     * @param date The date which should get converted
     * @return dateString The String containing the given date in the described format
     */
   private String getDateString(Date date){
       String dateString = "";
       if (date != null){
           SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
           dateString = formatter.format(date);
       }
       return dateString;
   }
}
