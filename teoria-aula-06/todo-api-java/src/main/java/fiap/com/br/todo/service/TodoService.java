package fiap.com.br.todo.service;

import fiap.com.br.todo.domain.todo.Todo;
import fiap.com.br.todo.domain.todo.TodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    public List<Todo> gettAllTodos(){

        return todoRepository.findAll();
    }
    public Todo createTodo(Todo todo){

        return todoRepository.save(todo);
    }
    public Todo markAsCompleted(Long id){
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ToDo not found"));
        todo.setCompleted(true);
        return todoRepository.save(todo);
    }
}
