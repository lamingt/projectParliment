package parliament;

import org.springframework.stereotype.Service;

@Service
public class ParliamentService {
    public final ParliamentRepository parliamentRepository;

    public ParliamentService(ParliamentRepository parliamentRepository) {
        this.parliamentRepository = parliamentRepository;
    }
}
