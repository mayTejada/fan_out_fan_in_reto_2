package reto.infrastructure.util;

import org.springframework.stereotype.Component;
import reto.domain.service.Fan;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación simple de Fan que distribuye los datos equitativamente entre los trabajadores.
 */

@Component
public class SimpleFan implements Fan {
    @Override
    public List<List<Long>> distributeNumbers(List<Long> inputs, int workers) {

        //Se inicializa una lista llamada distributed que contendrá tantas sublistas como trabajadores (workers).
        List<List<Long>> distributed = new ArrayList<>();

        //Se agregan workers sublistas vacías a distributed, una para cada trabajador.
        for (int i = 0; i < workers; i++) {
            distributed.add(new ArrayList<>());
        }
        for (int i = 0; i < inputs.size(); i++) {
            distributed.get(i % workers).add(inputs.get(i));
        }
        return distributed;
    }
}
