package pd.workshop.messageservice;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.LinkBuilder;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RestController;
import pd.workshop.common.WallMessage;

@RestController
@RequestMapping( "/messages" )
public class MessageRestController {

    private static Map <String, Message> messages = Collections.synchronizedMap( new HashMap <String, Message>() {{
        for( int i = 0; i < 20; i++ ) {
            Message message = Message.builder()
                    .idMessage( String.valueOf( i ) )
                    .title( "The message-" + i )
                    .text( "This is text " + i )
                    .build();
            put( message.getIdMessage(), message );
        }
    }} );
    Logger logger = LoggerFactory.getLogger( MessageRestController.class );
    @Value( "${format}" )
    private String format;

    @Autowired
    private WallClient wallClient;

    public static final LinkBuilder createLink() {
        return linkTo( MessageRestController.class );
    }

    public static final LinkBuilder createSingleLink( String id ) {
        return linkTo( methodOn( MessageRestController.class ).getMessage( id ) );
    }

    @RequestMapping( method = GET )
    public ResponseEntity <List <Message>> getAll() {
        return ResponseEntity.ok( messages.values().stream()
                .map( msg -> {
                    msg.add( createSingleLink( msg.getIdMessage() ).withSelfRel() );
                    return msg;
                } ).collect( Collectors.toList() ) );
    }

    @RequestMapping( method = GET, path = "/{id}" )
    public ResponseEntity <Message> getMessage( @PathVariable String id ) {
        Message message = messages.get( id );
        message.add( createSingleLink( id ).withSelfRel() );
        logger.warn( String.format( format, message.getTitle() ) );
        wallClient.update( WallMessage.builder()
                .messageId( message.getIdMessage() )
                .title( message.getTitle() )
                .build() );
        return ResponseEntity.ok( message );
    }
}
