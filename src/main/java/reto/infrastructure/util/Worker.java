package reto.infrastructure.util;

import org.springframework.stereotype.Component;
import reto.domain.service.WorkerInterface;

/**
 * Implementación de WorkerInterface que procesa un número elevándolo al cuadrado.
 */

@Component
public class Worker implements WorkerInterface {
    @Override
    public Long processSquareNumber(Long value) {
        long numberValue = value;
        return numberValue * numberValue;
    }
}
