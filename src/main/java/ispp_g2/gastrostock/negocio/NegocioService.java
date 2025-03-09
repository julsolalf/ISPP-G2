package ispp_g2.gastrostock.negocio;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class NegocioService {

    private NegocioRepository nr;

    @Autowired
    public NegocioService(NegocioRepository negocioRepository) {
        this.nr = negocioRepository;
    }

    
}
