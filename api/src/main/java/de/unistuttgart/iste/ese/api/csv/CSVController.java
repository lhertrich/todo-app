package de.unistuttgart.iste.ese.api.csv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The REST Controller class for the csv-export
 */
@RestController
public class CSVController {

    @Autowired
    private CSVService csvService;
    
    @GetMapping("/csv-downloads/todos")
    public void getTodosAsCSV(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv; charset=UTF-8");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"todos.csv\"");
        csvService.writeTodosToCSV(servletResponse.getWriter());
    }
}
