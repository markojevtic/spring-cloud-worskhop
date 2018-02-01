package pd.workshop.messageservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.hateoas.ResourceSupport;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message extends ResourceSupport {
    @NonNull
    private String idMessage;
    private String title;
    private String text;
}
