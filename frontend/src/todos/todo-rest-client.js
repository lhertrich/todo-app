import axios from "axios";
import config from "@/config";

/**
 * A file for functions which communicate with the todos backend and are used multiple times in different vues
 */
// send GET request to API to fetch todo with corresponding id
export async function fetchTodoFromId(id) {
    const response = await axios.get(`${config.apiBaseUrl}/todos/` + id);
    return response.data;
}

// send GET request to API to fetch all todos
export async function fetchAllTodos() {
    const response = await axios.get(`${config.apiBaseUrl}/todos`);
    return response.data;
}

// send GET request to API to get a csv file of all todos and download it 
export function csvExportTodos() {
    axios.get(`${config.apiBaseUrl}/csv-downloads/todos`).then((response) => {
        const anchor = document.createElement('a');
        anchor.href = 'data:text/csv;charset=utf-8,' + encodeURIComponent(response.data);
        anchor.target = '_blank';
        anchor.download = 'todos.csv';
        anchor.click();
    });
}