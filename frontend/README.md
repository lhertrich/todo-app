# Vue.js Frontend

> This is an example of a single page application frontend with Vue.js v2. The UI is accessible under http://localhost:8000. It uses the api contained in this project, for more details look at the README.md in the api folder.

## Prerequisites

- Install [Node.js](https://nodejs.org) 
- Ensure that the root folder of the installation is added to you `PATH`. You can check with this command: `echo %PATH%` (or `echo $PATH` on Linux / Git Bash).

## Usage
```bash
To run the application navigate to the frontend folder and do the following steps

# First install dependencies by running the command
npm install

# Note: For functionality and data persistence it is necessary that the backend server is running while running the frontend.
# To start the backend server or for more information about it look at ../api/README.md

# To start with hot-reload for development accessible under http://localhost:8000 run
npm run serve

# To compile and minify for production --> ./dist folder
npm run build

# lint and fix files
npm run lint
```

After you run the project, you should be able to see the UI in your browser (http://localhost:8000).

## Documentation

- General Vue.js documentation: https://v2.vuejs.org/v2/guide
- Vue Router documentation: https://v3.router.vuejs.org
- Vue CLI documentation: https://cli.vuejs.org/guide
- Bootstrap CSS framework: https://getbootstrap.com/docs/4.6/layout/overview
- BootstrapVue UI components: https://bootstrap-vue.org/docs/components
- BootstrapVue Icons: https://bootstrap-vue.org/docs/icons
