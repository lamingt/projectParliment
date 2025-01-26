package project.parliament;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/parliament")
public class ParliamentController {

    ParliamentService parliamentService;

    public ParliamentController(ParliamentService parliamentService) {
        this.parliamentService = parliamentService;
    }

}
