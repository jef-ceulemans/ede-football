package fact.it.newsservice.controller;

import fact.it.newsservice.dto.NewsRequest;
import fact.it.newsservice.dto.NewsResponse;
import fact.it.newsservice.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;


    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<NewsResponse> getAllNews() {
        return newsService.getAllNews();
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NewsResponse> getNewsByAuthor
            (@RequestParam String author) {
        return newsService.getNewsByAuthor(author);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createNews
            (@RequestBody NewsRequest newsRequest) {
        newsService.createNews(newsRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String updateNews
            (@RequestBody NewsRequest newsRequest) {
        boolean result = newsService.updateNews(newsRequest);
        return (result ? "News succes edited": "News editing fail");
    }

}

