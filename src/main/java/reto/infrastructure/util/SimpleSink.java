package reto.infrastructure.util;

import org.springframework.stereotype.Component;
import reto.domain.service.Sink;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de Sink que recolecta y ordena los resultados.
 */
@Component
public class SimpleSink implements Sink {
    @Override
    public List<Long> collectAndSortNumbers(List<Long> results) {
        return results.stream().sorted().collect(Collectors.toList());
    }
}
