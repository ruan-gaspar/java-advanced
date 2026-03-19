package br.com.fiap.watchlist.services;

import br.com.fiap.watchlist.controllers.ToWatchController;
import br.com.fiap.watchlist.models.ToWatch;
import br.com.fiap.watchlist.repositories.ToWatchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.WatchService;
import java.util.List;

@Service
public class ToWatchService {
    private final ToWatchRepository toWatchRepository;

    public ToWatchService(ToWatchRepository toWatchRepository) {
        this.toWatchRepository = toWatchRepository;
    }
    public List<ToWatch> getAllAll(){
        return toWatchRepository.findAll();
    }
    public ToWatch createToWatch(ToWatch toWatch){
        return toWatchRepository.save(toWatch);
    }
    public ToWatch markAsWatched(Long id){
        ToWatch toWatch = toWatchRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filme não encontrado"));
        toWatch.setWatched(true);
        return toWatchRepository.save(toWatch);
    }

}
