/**
 * A file for functions which communicate with the assignee backend and are used multiple times in different vues
 */
// import library for HTTP requests
import axios from "axios";
// import configuration with API url; @ refers to the src directory
import config from "@/config";

// send GET request to API to fetch all assignees
export async function fetchAllAssignees() {
    const response = await axios.get(`${config.apiBaseUrl}/assignees`);
    return response.data;
}