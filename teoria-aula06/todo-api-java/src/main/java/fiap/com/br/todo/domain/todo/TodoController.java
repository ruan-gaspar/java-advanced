package fiap.com.br.todo.domain.todo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.gettAllTodos();
    }
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Todo createTodo(@RequestBody Todo todo){
        return todoService.createTodo(todo);
    }
    @PutMapping("{id}")
    public Todo markAs Completed(@PathVariable Long id) {
        return
    }

}
