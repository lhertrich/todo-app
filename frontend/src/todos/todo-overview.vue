<template>
    <div>
        <h1>ToDo</h1>

        <b-alert v-bind:show="todos.length === 0" variant="warning">
            No todo available on the server...
        </b-alert>

        <b-modal v-model="showModal" ref="delete-modal" hide-footer
                 :title=" 'Delete todo with ID '+todoToDelete.id+'?'">
            <p>Warning: This can not be undone</p>
            <div id="delete-modal-buttons" style="float: right">
                <b-button pill style="margin: 5px" v-on:click="hideDeleteModal">Cancel</b-button>
                <b-button pill style="margin: 5px" variant="danger" v-on:click="deleteTodo(todoToDelete.id)">Delete
                </b-button>
            </div>
        </b-modal>

        <b-row class="mb-2">
            <b-col lg="6">
                <b-form-input id="filter-input-not-finished-table"
                              v-model="filterString"
                              type="search"
                              placeholder="Search for title..."></b-form-input>
            </b-col>
            <b-col lg="6">
                <div id="top-buttons" style="float: right">
                    <b-button pill style="margin-right: 5px" variant="outline-primary" @click="csvExportTodos()">
                        <b-icon-download></b-icon-download>
                        Export Todos
                    </b-button>
                    <b-button pill style="margin-left: 5px" variant="outline-primary" :to="'/create-todo'">
                        <b-icon-plus-circle></b-icon-plus-circle>
                        Add Todo
                    </b-button>
                </div>
            </b-col>
        </b-row>

        <div id="not-finished-table">
            <b-table ref="notFinishedTable" head-variant="dark" bordered striped hover :items="notFinishedTodos"
                     :fields="fields"
                     :borderless="true" :filter="filterString" :filter-included-fields="filterAttribute"
                     :per-page="perPage" :current-page="notFinishedCurrentPage">
                <template #cell(checkbox)="row">
                    <b-form-checkbox v-on:change="setTodoFinished(row.item)"
                                     v-model="row.item.finished"></b-form-checkbox>
                </template>
                <template #cell(assigneeList)="row">
                    <div v-for="assignee in row.item.assigneeList" v-bind:key="assignee.id" style="margin: 5px">
                        <b-badge pill style="font-size: medium; font-weight: normal">{{ assignee.prename + " " + assignee.name }}</b-badge>
                    </div>
                </template>
                <template #cell(actions)="row">
                    <b-button v-b-tooltip.hover title="Edit Todo" class="rounded-circle px-2" variant="primary"
                              :to="'/todos/' + row.item.id"
                              style="margin: 5px">
                        <b-icon-pencil-square></b-icon-pencil-square>
                    </b-button>
                    <b-button v-b-tooltip.hover title="Delete Todo" class="rounded-circle px-2" variant="danger"
                              v-on:click="showDeleteModal(row.item)"
                              style="margin: 5px">
                        <b-icon-trash-fill></b-icon-trash-fill>
                    </b-button>
                </template>
            </b-table>
        </div>
        <div id="not-finished-table-pagination" class="row">
            <div class="col-5"></div>
            <b-pagination pills class="col-2" v-model="notFinishedCurrentPage"
                          :total-rows="notFinishedRows"
                          :per-page="perPage"></b-pagination>
            <div class="col-5"></div>
        </div>

        <h1 style="margin-top: 50px">Finished</h1>

        <b-row class="mb-2">
            <b-col lg="6">
                <b-form-input id="filter-input-finished-table"
                              v-model="finishedFilterString"
                              type="search"
                              placeholder="Search for title..."></b-form-input>
            </b-col>
        </b-row>
        <div id="finished-table">
            <b-table head-variant="dark" bordered striped hover :items="finishedTodos" :fields="fields"
                     :borderless="true" :filter="finishedFilterString"
                     :filter-included-fields="filterAttribute" :per-page="perPage"
                     :current-page="finishedCurrentPage">
                <template #cell(checkbox)="row">
                    <b-form-checkbox v-on:change="setTodoFinished(row.item)"
                                     v-model="row.item.finished"></b-form-checkbox>
                </template>
                <template #cell(assigneeList)="row">
                    <div v-for="assignee in row.item.assigneeList" v-bind:key="assignee.id" style="margin: 5px">
                        <b-badge pill style="font-size: medium; font-weight: normal;">{{ assignee.prename + " " + assignee.name }}</b-badge>
                    </div>
                </template>
                <template #cell(actions)="row">
                    <b-button class="rounded-circle px-2" variant="primary" :to="'/todos/' + row.item.id"
                              style="margin: 5px">
                        <b-icon-pencil-square></b-icon-pencil-square>
                    </b-button>
                    <b-button class="rounded-circle px-2" variant="danger" v-on:click="showDeleteModal(row.item)"
                              style="margin: 5px">
                        <b-icon-trash-fill></b-icon-trash-fill>
                    </b-button>
                </template>
            </b-table>
        </div>
        
        <div id="finished-table-pagination" class="row">
            <div class="col-5"></div>
            <b-pagination pills class="col-2" v-model="finishedCurrentPage"
                          :total-rows="finishedRows"
                          :per-page="perPage"></b-pagination>
            <div class="col-5"></div>
        </div>

    </div>
</template>
<script>
// import configuration with API url; @ refers to the src directory
import config from "@/config";
// import library for HTTP requests
import axios from "axios";
import {showToastMessage} from "@/js/util";
import {fetchAllTodos, csvExportTodos} from "@/todos/todo-rest-client";
import {getTodofields} from "@/todos/todo-data";

export default {
    name: "ShowTodos",
    data() {
        return {
            todos: [],
            finishedTodos: [],
            notFinishedTodos: [],
            filterString: "",
            finishedFilterString: "",
            filterAttribute: ["title"],
            perPage: 5,
            finishedCurrentPage: 1,
            notFinishedCurrentPage: 1,
            fields: [],
            todoToDelete: "",
            showModal: false
        }
    },
    methods: {
        deleteTodo: function (id) {
            this.hideDeleteModal();
            // send DELETE request to API to delete the assignee by ID
            axios
                .delete(`${config.apiBaseUrl}/todos/${id}`)
                .then(async () => {
                    // show success message
                    showToastMessage(this, "Alert", `Successfully deleted Todo with ID ${id}!`, "success");
                    this.todos = await fetchAllTodos();
                    this.splitTodos(this.todos);
                });

        },
        /**
         * Sets a todo finished or unfinished depending on the current status and saves it
         *
         * @param todo The todo which should be set to finished or unfinished
         */
        setTodoFinished: function (todo) {
            let todoToSave = {
                title: todo.title,
                description: todo.title,
                finished: todo.finished,
                assigneeIdList: todo.assigneeList.map(assignee => assignee.id),
                dueDate: todo.dueDate
            }
            //save changed todo
            axios
                .put(`${config.apiBaseUrl}/todos/${todo.id}`, todoToSave)
                .then(async () => {
                    this.todos = await fetchAllTodos();
                    this.splitTodos(this.todos);
                })
                .catch((error) => {
                    // show error message
                    const errObject = error.response.data.errors[0];
                    showToastMessage(this, "Alert", `Error: ${errObject.field} ${errObject.defaultMessage}!`, "danger");
                });
        },
        csvExportTodos: function () {
            csvExportTodos();
        },
        /*
        Sets finished and not finished todos from the todos array
         */
        splitTodos: function (todos) {
            this.finishedTodos = todos.filter(todo => todo.finished);
            this.notFinishedTodos = todos.filter(todo => !todo.finished);
        },
        showDeleteModal: function (todo) {
            this.todoToDelete = todo;
            this.showModal = true;
        },
        hideDeleteModal: function () {
            this.showModal = false;
        }
    },
    created: async function () {
        this.fields = getTodofields();
        this.todos = await fetchAllTodos();
        this.splitTodos(this.todos);

    },
    computed: {
        finishedRows: function () {
            return this.finishedTodos.length;
        },
        notFinishedRows: function () {
            return this.notFinishedTodos.length;
        }
    }
}
</script>

<style>

</style>