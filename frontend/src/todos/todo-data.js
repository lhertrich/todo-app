/**
 * A file for long data properties of todos which could be reused in other future vue files
 */

export function getTodofields() {
    return [
        {
            key: "checkbox",
            label: "Done"
        },
        {
            key: "id",
            label: "ID",
            sortable: true
        },
        {
            key: "title",
            label: "Title",
            sortable: true
        },
        {
            key: "description",
            label: "Description",
            sortable: true
        },
        {
            key: "assigneeList",
            label: "Assignees"
        },
        {
            key: "createdDate",
            label: "Created",
            sortable: true,
            formatter: value => {
                const date = new Date(value);
                return date.toLocaleDateString('de-DE')
            }
        },
        {
            key: "dueDate",
            label: "Deadline",
            sortable: true,
            formatter: value => {
                if (value === null){
                    return "-";
                }
                const date = new Date(value);
                return date.toLocaleDateString('de-DE')
            }
        },
        {
            key: "finishedDate",
            label: "Finished Date",
            sortable: true,
            formatter: value => {
                if (value === null) {
                    return "-";
                }
                const date = new Date(value);
                return date.toLocaleDateString('de-DE')
            }
        },
        {
            key: "actions",
            label: "Actions"
        }
    ]
}