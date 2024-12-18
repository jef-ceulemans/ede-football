package fact.it.newsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(value = "news")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class News {
    private String id;
    private String author;
    private String content;
    private String imageUrl;
    private LocalDateTime publishedDate;
    private LocalDateTime updatedDate;
}
