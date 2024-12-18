package fact.it.newsservice.service;

import fact.it.newsservice.dto.NewsRequest;
import fact.it.newsservice.dto.NewsResponse;
import fact.it.newsservice.model.News;
import fact.it.newsservice.repository.NewsRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    @PostConstruct
    public void loadData() {
        if (newsRepository.count() <= 0) {
            News news1 = News.builder()
                    .author("John Doe")
                    .content("Breaking news: Local team wins championship!")
                    .imageUrl("https://example.com/championship.jpg")
                    .publishedDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            News news2 = News.builder()
                    .author("Jane Smith")
                    .content("Transfer rumors: Star player to join a new club.")
                    .imageUrl("https://example.com/transfer.jpg")
                    .publishedDate(LocalDateTime.now().minusDays(1))
                    .updatedDate(LocalDateTime.now().minusDays(1))
                    .build();

            newsRepository.save(news1);
            newsRepository.save(news2);
        }
    }

    public void createNews(NewsRequest newsRequest) {
        News news = News.builder()
                .author(newsRequest.getAuthor())
                .content(newsRequest.getContent())
                .imageUrl(newsRequest.getImageUrl())
                .publishedDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        newsRepository.save(news);
    }

    public boolean updateNews(NewsRequest newsRequest) {
        // Haal het nieuws op via de id van de NewsRequest
        News existingNews = newsRepository.findById(newsRequest.getId())
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + newsRequest.getId()));

        // Update de velden
        existingNews.setAuthor(newsRequest.getAuthor());
        existingNews.setContent(newsRequest.getContent());
        existingNews.setImageUrl(newsRequest.getImageUrl());
        existingNews.setUpdatedDate(LocalDateTime.now());

        // Sla het bijgewerkte nieuws op in de repository
        newsRepository.save(existingNews);

        return true; // of false, afhankelijk van het succes van de update
    }

    public List<NewsResponse> getAllNews() {
        List<News> newsList = newsRepository.findAll();
        return newsList.stream()
                .map(this::mapToNewsResponse)
                .toList();
    }

    public List<NewsResponse> getNewsByAuthor(String author) {
        List<News> newsList = newsRepository.findNewsByAuthor(author);
        return newsList.stream()
                .map(this::mapToNewsResponse)
                .toList();
    }

    private NewsResponse mapToNewsResponse(News news) {
        return NewsResponse.builder()
                .id(news.getId())
                .author(news.getAuthor())
                .content(news.getContent())
                .imageUrl(news.getImageUrl())
                .publishedDate(news.getPublishedDate())
                .updatedDate(news.getUpdatedDate())
                .build();
    }
}
