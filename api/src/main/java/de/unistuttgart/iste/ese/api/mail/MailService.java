package de.unistuttgart.iste.ese.api.mail;

import de.unistuttgart.iste.ese.api.assignees.Assignee;
import de.unistuttgart.iste.ese.api.csv.CSVService;
import de.unistuttgart.iste.ese.api.todos.ToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * The MailService class for the application, a classical spring component which can be injected 
 * It uses the properties mail.senderAddress, mail.username and mail.password which has to be defined in the application properties
 */
@Component
public class MailService {

    //Getting the necessary attributes from the application properties with the @Value notation
    @Value( "${mail.senderAddress}")
    private String senderAddress;

    @Value( "${mail.username}" )
    private String username;

    @Value( "${mail.password}" )
    private String password;
    
    private Session session;
    
    @Autowired
    private CSVService csvService;

    /**
     * The constructor for the MailService which initializes the properties and instantiates a session
     */
    public MailService(){
        // instantiate SMTP properties
        Properties props = this.initProperties();

        // get a Session object with the properties
        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    /**
     * This method sends a mail to the email address of a given assignee which informs him that he was 
     * assigned to a given todo together with information about this todo
     * 
     * @param assignee The assignee which should get informed
     * @param toDo The todo to which the assignee has been assigned
     */
    public void sendMail(Assignee assignee, ToDo toDo){
        try {
            // create a default MimeMessage object and fill its attributes
            Message message = new MimeMessage(this.session);
            message.setFrom(new InternetAddress(this.senderAddress));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(assignee.getEmail()));
            message.setSubject("[TodoApp] New assignment to todo");
            message.setText(this.getMailString(toDo, assignee));
            // send message
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * A helper method which initializes the properties of the smtp mail server of the university of stuttgart and returns them
     * 
     * @return props the initialzied properties of the smtp mail server of the university of stuttgart
     */
    private Properties initProperties(){
        //SMTP properties from https://www.tik.uni-stuttgart.de/en/support/service-manuals/e-mail/)
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.uni-stuttgart.de");
        props.put("mail.smtp.port", "587");
        return props;
    }
    
    /*
     * A helper method which returns a string which is sent to the assignees which should be informed about their new assignment
     */
    private String getMailString(ToDo toDo, Assignee assignee){
        String finishedDate = "";
        if (!toDo.isFinished()){
            finishedDate = "The todo is not finished yet";
        } else {
            finishedDate = new SimpleDateFormat("yyyy-MM-dd").format(toDo.getFinishedDate());
        }
        String dueDate = "";
        if (toDo.getDueDate() == null || toDo.getDueDate().equals(new Date(0))){
            dueDate = "No due date set";
        } else {
            dueDate = new SimpleDateFormat("yyyy-MM-dd").format(toDo.getDueDate());
        }
        return "Hi "+assignee.getPrename()+" "+assignee.getName()+", you are assigned to todo "+toDo.getId()+":\n\nTitle: "+toDo.getTitle()+"\n" +
            "Description: "+toDo.getDescription()+"\nFinished: "+toDo.isFinished()+"\nAssignees: "
            +csvService.getAssigneeString(toDo.getAssignees())+"\nCreated at: "+new SimpleDateFormat("yyyy-MM-dd").format(toDo.getCreatedDate())+"\nDeadline: "
            +dueDate+"\nFinished at: "+finishedDate+"\n\nCheck out the todo app to see more details and edit the todo";
    }
}
