package pd.workshop.wallservice;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RestController;
import pd.workshop.common.WallMessage;

@RestController
@RequestMapping( "/wall" )
public class WallRestController {
    private static final Map <String, WallItem> frequency = Collections.synchronizedMap( new HashMap <>() );

    @RequestMapping( method = POST )
    public ResponseEntity <Void> add( @RequestBody WallMessage visitedMessage ) {
        increment( visitedMessage );
        return ResponseEntity.ok( null );
    }

    private void increment( WallMessage visitedMessage ) {
        frequency.putIfAbsent( visitedMessage.getMessageId(), createNewItem( visitedMessage ) );
        WallItem wallItem = frequency.get( visitedMessage.getMessageId() );
        wallItem.getInteger().incrementAndGet();
    }

    private WallItem createNewItem( WallMessage visitedMessage ) {
        return WallItem.builder()
                .integer( new AtomicInteger( 0 ) )
                .messageId( visitedMessage.getMessageId() )
                .title( visitedMessage.getTitle() )
                .build();
    }

    @RequestMapping(method = GET)
    public ResponseEntity<List<WallItem>> getAll() {
        return ResponseEntity.ok( frequency.values().stream()
                .sorted( (x,y) -> x.getInteger().get() - y.getInteger().get() )
                .collect(toList()) );
    }
}
