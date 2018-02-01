package pd.workshop.wallservice;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WallItem extends ResourceSupport {
    private String messageId;
    private String title;
    private AtomicInteger integer;
}
