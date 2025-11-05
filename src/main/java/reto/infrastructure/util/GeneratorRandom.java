package reto.infrastructure.util;

import java.util.List;
import java.util.Random;

/**
 * Utilidad para generar listas de números aleatorios en un rango dado.
 */
public class GeneratorRandom {
    public static List<Long> generateRandomNumbers(Long sizeListNumbers,
                                                   Long rangeMinGenerateRandom,
                                                   Long rangeMaxGenerateRandom) {

        return new Random().longs(sizeListNumbers, rangeMinGenerateRandom, rangeMaxGenerateRandom)
                .boxed()
                .toList();
    }
}
