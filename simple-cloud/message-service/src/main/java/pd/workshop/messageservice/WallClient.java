package pd.workshop.messageservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import pd.workshop.common.WallMessage;

@FeignClient("wallservice")
public interface WallClient {
    @RequestMapping(method = POST, value = "/wall" )
    void update(WallMessage msg);
}