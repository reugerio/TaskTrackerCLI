# Task Tracker CLI

A simple java application to manage tasks from the command line. It allows users to add, update, delete, and mark tasks as done, in-progress, or todo.

Prerequisites
- Java 25 or higher

How to run

Clone Repository

git clone https://github.com/reugerio/TaskTrackerCLI.git

Run the following command to run the application:

### Add Task
##### java src/Main.java add `description`
###### Example: 
- java src/Main.java add "Buy an apple"

### Update a task
##### java src/Main.java update `id` `description`
###### Example: 
- java src/Main.java update 1 "Buy a banana"

### Delete a task
##### java src/Main.java delete `id`
###### Example: 
- java src/Main.java delete 1

### Mark a task as in progress/done
##### java src/Main.java mark-done/mark-in-progress`id`
###### Example: 
- java src/Main.java mark-done 1
- java src/Main.java mark-in-progress 1

### List all tasks
##### java src/Main.java list `status (optional)` 
###### Example: 
- java src/Main.java list
- java src/Main.java list in-progress
- java src/Main.java list todo
- java src/Main.java list done