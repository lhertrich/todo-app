import Vue from "vue";
import VueRouter from "vue-router";
import ShowAssignees from "../assignees/assignee-overview";
import CreateAssignee from "../assignees/assignee-create";
import DetailAssignee from "../assignees/assignee-detail";
import ShowTodos from "../todos/todo-overview";
import CreateTodo from "../todos/todo-create";
import DetailTodo from "../todos/todo-detail";

Vue.use(VueRouter);

// all frontend routes of the app, i.e. bind a path to a Vue component
const routes = [
    {
        path: "/",
        name: "HomePage",
        component: ShowTodos
    },
    {
        path: "/todos",
        name: "ShowTodos",
        component: ShowTodos
    },
    {
        path: "/assignees",
        name: "ShowAssignees",
        component: ShowAssignees
    },
    {
        path: "/create-assignee",
        name: "CreateAssignee",
        component: CreateAssignee
    },
    {
        path: "/assignees/:id",
        name: "DetailAssignee",
        component: DetailAssignee
    },
    {
        path: "/create-todo",
        name: "CreateTodo",
        component: CreateTodo
    },
    {
        path: "/todos/:id",
        name: "DetailTodo",
        component: DetailTodo
    }
];

const router = new VueRouter({
    routes
});

export default router;
