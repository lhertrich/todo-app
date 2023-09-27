<template>
    <div>
        <h1>Assignees</h1>

        <b-alert v-bind:show="assignees.length === 0" variant="warning">
            No assignee available on the server...
        </b-alert>

        <b-modal v-model="showModal" ref="delete-modal" hide-footer
                 :title=" 'Delete todo with ID '+assigneeToDelete.id+'?'">
            <p>Warning: This can not be undone</p>
            <div id="delete-modal-buttons" style="float: right">
                <b-button style="margin: 5px" v-on:click="hideDeleteModal">Cancel</b-button>
                <b-button style="margin: 5px" variant="danger" v-on:click="deleteAssignee(assigneeToDelete.id)">Delete
                </b-button>
            </div>
        </b-modal>
        
        <b-row class="mb-2">
            <b-col lg="6">
                <b-form-input id="filter-input"
                              v-model="filterString"
                              type="search"
                              placeholder="Search Assignee..."></b-form-input>
            </b-col>
            <b-col lg="6">
                <div style="float: right">
                    <b-button pill variant="outline-primary" :to="'/create-assignee'">
                        <b-icon-plus-circle style="align-self: center"></b-icon-plus-circle>
                        Add Assignee
                    </b-button>
                </div>
            </b-col>
        </b-row>

        <div id="assignee-table">
            <b-table head-variant="dark" bordered striped hover :items="assignees" :fields="fields"
                     :borderless="true" :filter="filterString" :filter-included-fields="filterAttribute"
                     :per-page="perPage" :current-page="currentPage">
                <template #cell(actions)="row">
                    <b-button v-b-tooltip.hover title="Edit Assignee" class="rounded-circle px-2" variant="primary"
                              :to="'/assignees/' + row.item.id"
                              style="margin: 5px">
                        <b-icon-pencil-square></b-icon-pencil-square>
                    </b-button>
                    <b-button v-b-tooltip.hover title="Delete Assignee" class="rounded-circle px-2" variant="danger"
                              v-on:click="showDeleteModal(row.item)"
                              style="margin: 5px">
                        <b-icon-trash-fill></b-icon-trash-fill>
                    </b-button>
                </template>
            </b-table>
        </div>

        <div id="pagination" class="row">
            <div class="col-5"></div>
            <b-pagination pills class="col-2" v-model="currentPage"
                          :total-rows="rows"
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
import {fetchAllAssignees} from "./assignee-rest-client";
import {getAssigneefields} from "@/assignees/assignee-data";

export default {
    name: "ShowAssignees",
    data() {
        return {
            assignees: [],
            filterString: "",
            filterAttribute: ["prename", "name", "email"],
            perPage: 5,
            currentPage: 1,
            fields: [],
            showModal: false,
            assigneeToDelete: ""
        };
    },
    methods: {
        deleteAssignee: function (id) {
            this.hideDeleteModal();
            // send DELETE request to API to delete the assignee by ID
            axios
                .delete(`${config.apiBaseUrl}/assignees/${id}`)
                .then(async () => {
                    // show success message
                    showToastMessage(this, "Alert", `Successfully deleted assignee with ID ${id}!`, "success"
                    );
                    this.assignees = await fetchAllAssignees();
                });
        },
        showDeleteModal: function (assignee) {
            this.assigneeToDelete = assignee;
            this.showModal = true;
        },
        hideDeleteModal: function () {
            this.showModal = false;
        }
    },
    created: async function () {
        this.fields = getAssigneefields();
        this.assignees = await fetchAllAssignees();
    },
    computed: {
        rows: function () {
            return this.assignees.length;
        }
    }
}
</script>

<style>

</style>