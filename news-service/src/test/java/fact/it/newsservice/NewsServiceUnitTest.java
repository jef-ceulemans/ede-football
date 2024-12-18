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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewsServiceUnitTest {

    @InjectMocks
    private NewsService newsService;

    @Mock
    private NewsRepository newsRepository;

}