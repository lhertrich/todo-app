<template>
    <div id="app">
        <!-- this navigation will appear on every route -->

        <b-navbar toggleable="lg" type="dark" variant="dark">
            <b-navbar-brand to="/todos">
                <b-icon-check2-all style="margin-right: 5px"></b-icon-check2-all>
                Todo
            </b-navbar-brand>

            <b-navbar-brand to="/assignees">
                Assignees
            </b-navbar-brand>

            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
            
        </b-navbar>

        <!-- the different view templates will appear in this HTML element based on the route -->
        <div class="main">
            <router-view></router-view>
        </div>
    </div>
</template>

<style>
#app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    color: #2c3e50;
}

.main {
    padding: 10px 20px;
}

</style>
