<template>
    <div class="container">
        <div class="row">
            <div class="col-2"></div>
            <div class="col-8">

                <div class="row">
                    <h1 class="col">Detailed View of: {{ assignee.prename + " " + assignee.name }}</h1>
                </div>

                <div>
                    <b-form @submit="onSubmit">

                        <div class="row">
                            <b-form-group class="col" label="Prename:">
                                <b-form-input
                                    v-model="assignee.prename"
                                    type="text"
                                    required
                                ></b-form-input>
                            </b-form-group>
                        </div>

                        <div class="row">
                            <b-form-group class="col" label="Name:">
                                <b-form-input
                                    v-model="assignee.name"
                                    type="text"
                                    required
                                ></b-form-input>
                            </b-form-group>
                        </div>

                        <div class="row">
                            <b-form-group class="col" label="Email:">
                                <b-form-input
                                    v-model="assignee.email"
                                    type="text"
                                    required
                                ></b-form-input>
                            </b-form-group>
                        </div>

                        <div class="row">
                            <div class="col"></div>
                            <b-button class="col-2" :to="'/assignees'" style="margin: 5px">Cancel</b-button>
                            <b-button class="col-2" type="submit" variant="success" style="margin: 5px">Save</b-button>
                        </div>

                    </b-form>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
// import configuration with API url; @ refers to the src directory
import config from "@/config";
// import library for HTTP requests
import axios from "axios";
// import JS method to use in this view
import {showToastMessage} from "@/js/util";

export default {
    name: "DetailAssignee",
    data() {
        return {
            assignee: {
                prename: "",
                name: "",
                email: ""
            }
        };
    },
    methods: {
        // executed on form submit
        onSubmit: function (event) {
            event.preventDefault();
            axios
                .put(`${config.apiBaseUrl}/assignees/${this.assignee.id}`, this.assignee)
                .then((response) => {
                    // show success message
                    showToastMessage(this, "Alert", `Successfully updated assignee with ID ${response.data.id}!`, "success"
                    );
                    // navigate to assignee list view
                    this.$router.push(`/assignees`);
                })
                .catch((error) => {
                    // show error message
                    const errObject = error.response.data.errors[0];
                    showToastMessage(this, "Alert", `Error: ${errObject.field} ${errObject.defaultMessage}!`, "danger"
                    );
                });
        }
    },
    // executed after the component has been started
    created: function () {
        axios
            .get(`${config.apiBaseUrl}/assignees/${this.$route.params.id}`)
            .then((response) => {
                // assign response to data variable
                this.assignee = response.data;
            });
    }
};
</script>

<style>

</style>