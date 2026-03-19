package br.com.fiap.watchlist.controllers;

import br.com.fiap.watchlist.models.ToWatch;
import br.com.fiap.watchlist.services.ToWatchService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.nio.file.WatchService;
import java.util.List;

@RestController
@RequestMapping("/api/to-watch")
@CrossOrigin(origins = "http://localhost:4200")
public class ToWatchController {
    private final ToWatchService toWatchService;

    public ToWatchController(ToWatchService toWatchService) {
        this.toWatchService = toWatchService;
    }
    @GetMapping
    public List<ToWatch> getAllAll() {
        return toWatchService.getAllAll();
    }
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ToWatch create(@RequestBody ToWatch toWatch) {
        return toWatchService.createToWatch(toWatch);
    }
    @PutMapping("{id}")
    public ToWatch markAsWatched(@PathVariable Long id) {
        return toWatchService.markAsWatched(id);
    }
}
