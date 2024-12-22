package fact.it.newsservice;

import fact.it.newsservice.dto.NewsRequest;
import fact.it.newsservice.dto.NewsResponse;
import fact.it.newsservice.model.News;
import fact.it.newsservice.repository.NewsRepository;
import fact.it.newsservice.service.NewsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewsServiceUnitTest {

    @InjectMocks
    private NewsService newsService;

    @Mock
    private NewsRepository newsRepository;

    @Test
    public void testCreateNews() {
        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setAuthor("Test Author");
        newsRequest.setContent("Test Content");
        newsRequest.setImageUrl("https://example.com/test.jpg");

        News news = News.builder()
                .author("Test Author")
                .content("Test Content")
                .imageUrl("https://example.com/test.jpg")
                .publishedDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        when(newsRepository.save(any(News.class))).thenReturn(news);

        newsService.createNews(newsRequest);

        verify(newsRepository, times(1)).save(any(News.class));
    }

    @Test
    public void testUpdateNews() {
        News existingNews = News.builder()
                .id(1L)
                .author("Old Author")
                .content("Old Content")
                .imageUrl("https://example.com/old.jpg")
                .publishedDate(LocalDateTime.now().minusDays(1))
                .updatedDate(LocalDateTime.now().minusDays(1))
                .build();

        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setId(1L);
        newsRequest.setAuthor("New Author");
        newsRequest.setContent("New Content");
        newsRequest.setImageUrl("https://example.com/new.jpg");

        when(newsRepository.findById(1L)).thenReturn(Optional.of(existingNews));
        when(newsRepository.save(any(News.class))).thenReturn(existingNews);

        boolean result = newsService.updateNews(newsRequest);

        assertTrue(result);
        verify(newsRepository, times(1)).findById(1L);
        verify(newsRepository, times(1)).save(existingNews);
        assertEquals("New Author", existingNews.getAuthor());
        assertEquals("New Content", existingNews.getContent());
        assertEquals("https://example.com/new.jpg", existingNews.getImageUrl());
    }

    @Test
    public void testGetAllNews() {
        News news1 = News.builder()
                .id(1L)
                .author("Author 1")
                .content("Content 1")
                .imageUrl("https://example.com/1.jpg")
                .publishedDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        News news2 = News.builder()
                .id(2L)
                .author("Author 2")
                .content("Content 2")
                .imageUrl("https://example.com/2.jpg")
                .publishedDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        when(newsRepository.findAll()).thenReturn(Arrays.asList(news1, news2));

        List<NewsResponse> newsResponses = newsService.getAllNews();

        assertEquals(2, newsResponses.size());
        assertEquals("Author 1", newsResponses.get(0).getAuthor());
        assertEquals("Author 2", newsResponses.get(1).getAuthor());
        verify(newsRepository, times(1)).findAll();
    }

    @Test
    public void testGetNewsByAuthor() {
        String author = "Specific Author";

        News news = News.builder()
                .id(1L)
                .author(author)
                .content("Content")
                .imageUrl("https://example.com/image.jpg")
                .publishedDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        when(newsRepository.findNewsByAuthor(author)).thenReturn(List.of(news));

        List<NewsResponse> newsResponses = newsService.getNewsByAuthor(author);

        assertEquals(1, newsResponses.size());
        assertEquals(author, newsResponses.get(0).getAuthor());
        verify(newsRepository, times(1)).findNewsByAuthor(author);
    }
}
