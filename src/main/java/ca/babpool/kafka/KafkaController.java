package ca.babpool.kafka;

import ca.babpool.model.response.ApiResponse;
import ca.babpool.model.response.CommonResult;
import ca.babpool.model.response.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class KafkaController {
    private final KafkaProducer producer;
    private final ApiResponse apiResponse;

    @PostMapping
    public SingleResult<String> sendMessage(@RequestBody String message) {
        this.producer.sendMessage(message);

        return apiResponse.getSingleResult("success");
    }
}