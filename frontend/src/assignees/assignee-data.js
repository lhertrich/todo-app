/**
 * A file for long data properties of assignees which could be reused in other future vue files
 */

export function getAssigneefields(){
    return [
        {
            key: "id",
            label: "ID",
            sortable: true
        },
        {
            key: "prename",
            label: "First Name",
            sortable: true
        },
        {
            key: "name",
            label: "Last Name",
            sortable: true
        },
        {
            key: "email",
            label: "Email",
            sortable: true
        },
        {
            key: "actions",
            label: "Actions"
        }
    ]
}