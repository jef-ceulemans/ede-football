package fact.it.newsservice.repository;

import fact.it.newsservice.model.News;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NewsRepository extends MongoRepository<News, String> {
    List<News> findNewsByAuthor(String author);
}
