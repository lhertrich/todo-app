<template>
    <b-overlay :show="showOverlay" rounded="sm">
        <div class="container">
            <div class="row">
                <div class="col-2"></div>
                <div class="col-8">

                    <div class="row">
                        <h1 class="col">Detailed View for: {{ editTodo.title }} </h1>
                    </div>

                    <div>
                        <b-form @submit="onSubmit">

                            <div id="title-form" class="row">
                                <b-form-group class="col" label="Title:">
                                    <b-form-input
                                        v-model="editTodo.title"
                                        type="text"
                                        required
                                    ></b-form-input>
                                </b-form-group>
                            </div>

                            <div id="description-form" class="row">
                                <b-form-group class="col" label="Description:">
                                    <b-form-textarea
                                        v-model="editTodo.description"
                                        type="text"
                                        required
                                    ></b-form-textarea>
                                </b-form-group>
                            </div>

                            <div id="assignees-form" class="row">
                                <b-form-group class="col" label="Assignees:">
                                    <div style="margin-bottom: 10px">
                                        <b-badge pill v-for="assignee in selectedAssignees" v-bind:key="assignee.id"
                                                 style="margin: 2px; font-size: medium; font-weight: normal">
                                            {{ assignee.prename }} {{ assignee.name }}
                                        </b-badge>
                                    </div>
                                    <b-dropdown text="Choose Assignees">
                                        <b-dropdown-item-button v-on:click="selectAllAssignees">Select All
                                        </b-dropdown-item-button>
                                        <b-dropdown-divider></b-dropdown-divider>
                                        <b-dropdown-item-button v-on:click="deselectAllAssignees">Deselect All
                                        </b-dropdown-item-button>
                                        <b-dropdown-divider></b-dropdown-divider>
                                        <b-dropdown-item v-if="assignees.length === 0" disabled>No existing assignees
                                        </b-dropdown-item>
                                        <b-dropdown-item-button pill v-for="assignee in assignees"
                                                                v-bind:key="assignee.id"
                                                                v-on:click="selectAssignee(assignee)" variant="none"
                                                                :class="{'btn-primary': selectedAssignees.map(ass => ass.id).includes(assignee.id)}"
                                                                style="margin: 2px; border-radius: 4px">
                                            {{ assignee.prename }} {{ assignee.name }}
                                        </b-dropdown-item-button>
                                    </b-dropdown>
                                </b-form-group>
                            </div>

                            <div id="date-form" class="row">
                                <b-form-group class="col" label="DueDate:">
                                    <b-form-datepicker v-model="editTodo.dueDate" type="date"
                                                       required></b-form-datepicker>
                                </b-form-group>
                            </div>

                            <div class="row">
                                <div class="col"></div>
                                <b-button class="col-2" :to="'/todos'" style="margin: 5px">Cancel</b-button>
                                <b-button class="col-2" type="submit" variant="success" style="margin: 5px">Save
                                </b-button>
                            </div>

                        </b-form>
                    </div>
                </div>
                <div class="col-2"></div>
            </div>
        </div>
    </b-overlay>
</template>

<script>
// import configuration with API url; @ refers to the src directory
import config from "@/config";
// import library for HTTP requests
import axios from "axios";
// import JS method to use in this view
import {showToastMessage} from "@/js/util";
import {fetchAllAssignees} from "@/assignees/assignee-rest-client";
import {fetchTodoFromId} from "@/todos/todo-rest-client";

export default {
    name: "DetailTodo",
    data() {
        return {
            assignees: [],
            selectedAssignees: [],
            showOverlay: false,
            saveTodo: {
                title: "",
                description: "",
                finished: "",
                assigneeIdList: [],
                dueDate: ""
            },
            editTodo: {
                id: 0,
                title: "",
                description: "",
                finished: "",
                assigneeList: [],
                dueDate: ""
            }
        }
    },
    methods: {
        // executed on form submit
        onSubmit: function (event) {
            //show overlay
            this.showOverlay = true;
            event.preventDefault();
            this.createSaveTodo();
            axios
                .put(`${config.apiBaseUrl}/todos/${this.editTodo.id}`, this.saveTodo)
                .then((response) => {
                    // show success message
                    showToastMessage(this, "Alert", `Successfully updated todo with ID ${response.data.id}!`, "success");
                    // navigate to assignee list view
                    this.$router.push(`/todos`);
                })
                .catch((error) => {
                    // show error message
                    const errObject = error.response.data.errors[0];
                    showToastMessage(this, "Alert", `Error: ${errObject.field} ${errObject.defaultMessage}!`, "danger");
                });
        },
        /**
         Creates a saveTodo which contains all properties of the editTodo but maps the assignees to their ids
         This is necessary, because the editTodo has no assigneeIdList field which is required from the backend
         */
        createSaveTodo: function () {
            this.saveTodo.title = this.editTodo.title;
            this.saveTodo.description = this.editTodo.description;
            this.saveTodo.finished = this.editTodo.finished;
            this.saveTodo.assigneeIdList = this.selectedAssignees.map(assignee => assignee.id);
            this.saveTodo.dueDate = this.editTodo.dueDate;
        },
        /**
         * This function adds an assignee to the selectedAssignees array or remove one if already contained in the array
         * It is used to enable the selection of assignees for a todo via dropdown
         *
         * @param assignee The assignee which was selected and should be added or removed from the selectedAssignees
         */
        selectAssignee: function (assignee) {
            let selectedAssigneeIds = this.selectedAssignees.map(assignee => assignee.id);
            if (selectedAssigneeIds.includes(assignee.id)) {
                let index = selectedAssigneeIds.indexOf(assignee.id);
                this.selectedAssignees.splice(index, 1);
            } else {
                this.selectedAssignees.push(assignee);
            }
        },
        selectAllAssignees: function () {
            this.selectedAssignees = [...this.assignees];
        },
        deselectAllAssignees: function () {
            this.selectedAssignees = [];
        }
    },
    created: async function () {
        this.assignees = await fetchAllAssignees();
        this.editTodo = await fetchTodoFromId(this.$route.params.id);
        if (this.editTodo.dueDate === null){
            this.editTodo.dueDate = "";
        } else {
            this.editTodo.dueDate = new Date(this.editTodo.dueDate).toISOString().substring(0, 10);
        }
        this.selectedAssignees = this.editTodo.assigneeList;
    }
};
</script>

<style scoped>

</style>