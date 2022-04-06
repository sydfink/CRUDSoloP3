package com.galvanize.SoloP3;


import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class TodoController {
    TodoRepository repository;

    public TodoController(TodoRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/todo")
    public Todo addNewToDo(@RequestBody Todo todo){
        return repository.save(todo);
    }

    @GetMapping("/todo/{id}")
    public Optional<Todo> getToDoById(@PathVariable Long id){
        return repository.findById(id);
    }

    @GetMapping("/todo")
    public Iterable<Todo> getAllToDo(){
        return repository.findAll();
    }

    @PatchMapping("/todo/{id}")
    public Todo updateOneToDo(@PathVariable Long id, @RequestBody Map<String, String> todos){
        Todo todo = this.repository.findById(id).get();
        //check passed in variable info to update toDoList
        todos.forEach((key, value) -> {
            if (key.equals("description")){
                todo.setDescription(value);
            } else if ((key.equals("dueDate"))) {
                todo.setDueDate(todo.getDueDate());
            } else if ((key.equals("priority"))){
                todo.setPriority(value);
            }
        });
        //save changed entry for toDoList
        return this.repository.save(todo);
    }

    @DeleteMapping("/todo/{id}")
    public void deleteOneToDo(@PathVariable Long id){
        repository.deleteById(id);
    }

}
