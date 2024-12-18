package fact.it.newsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponse {
    private String id;
    private String author;
    private String content;
    private String imageUrl;
    private LocalDateTime publishedDate;
    private LocalDateTime updatedDate;
}