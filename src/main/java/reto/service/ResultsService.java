package reto.service;

import org.springframework.beans.factory.annotation.Autowired;
import reto.domain.service.Fan;
import reto.domain.service.Sink;
import reto.domain.service.WorkerInterface;
import reto.infrastructure.util.GeneratorRandom;
import reto.exception.ErrorMessages;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Servicio encargado de procesar una lista de números, distribuyéndolos entre varios trabajadores
 * para su procesamiento concurrente y devolviendo los resultados ordenados.
 */
@Service
public class ResultsService {
    private static final int MIN_WORKERS = 3;
    private static final int MAX_WORKERS = 8;

    private final Fan fan;
    private final WorkerInterface worker;
    private final Sink sink;

    /**
     * Constructor que inyecta las dependencias principales.
     *
     * @param fan Distribuidor de números entre trabajadores.
     * @param worker Lógica de procesamiento de cada número.
     * @param sink Recolector y ordenador de resultados.
     */

    @Autowired
    public ResultsService(Fan fan, WorkerInterface worker, Sink sink) {
        this.fan = fan;
        this.worker = worker;
        this.sink = sink;
    }

    /**
     * Procesa los números dados o los genera aleatoriamente, los distribuye entre varios hilos,
     * los procesa y retorna los resultados ordenados.
     *
     * @param numbers Lista de números a procesar (puede ser null o vacía)
     * @param sizeListNumbers Cantidad de números a generar si la lista es vacía
     * @param rangeMinGenerateRandom Valor mínimo para generación aleatoria
     * @param rangeMaxGenerateRandom Valor máximo para generación aleatoria
     * @return Lista de resultados procesados y ordenados
     * @throws InterruptedException Si la ejecución es interrumpida
     */
    public List<Long> resultsSquareNumbers(List<Long> numbers,
                                           long sizeListNumbers,
                                           long rangeMinGenerateRandom,
                                           long rangeMaxGenerateRandom) throws InterruptedException {
        int workers = Math.max(MIN_WORKERS, Math.min(Runtime.getRuntime().availableProcessors(), MAX_WORKERS));
        List<Long> inputs = prepareInput(numbers, sizeListNumbers, rangeMinGenerateRandom, rangeMaxGenerateRandom);
        List<List<Long>> partitions = fan.distributeNumbers(inputs, workers);

        return processPartitions(partitions, workers);
    }

    /**
     * Prepara la lista de entrada. Si la lista es nula o vacía, genera números aleatorios.
     *
     * @param numbers Lista de números original.
     * @param sizeListNumbers Cantidad de números a generar si la lista es vacía.
     * @param rangeMinGenerateRandom Valor mínimo para generación aleatoria.
     * @param rangeMaxGenerateRandom Valor máximo para generación aleatoria.
     * @return Lista de números lista para procesar.
     */
    private List<Long> prepareInput(List<Long> numbers, long sizeListNumbers, long rangeMinGenerateRandom, long rangeMaxGenerateRandom) {
        if (numbers == null || numbers.isEmpty()) {
            validateGenerationParams(sizeListNumbers, rangeMinGenerateRandom, rangeMaxGenerateRandom);
            return GeneratorRandom.generateRandomNumbers(sizeListNumbers, rangeMinGenerateRandom, rangeMaxGenerateRandom);
        }
        return numbers;
    }

    /**
     * Valida los parámetros para la generación de números aleatorios.
     *
     * @param sizeListNumbers Cantidad de números a generar.
     * @param rangeMinGenerateRandom Valor mínimo permitido.
     * @param rangeMaxGenerateRandom Valor máximo permitido.
     * @throws IllegalArgumentException Si los parámetros no son válidos.
     */
    private void validateGenerationParams(long sizeListNumbers, long rangeMinGenerateRandom, long rangeMaxGenerateRandom) {
        if (sizeListNumbers <= 0) {
            throw new IllegalArgumentException(ErrorMessages.SIZE_MUST_BE_POSITIVE);
        }
        if (rangeMaxGenerateRandom <= rangeMinGenerateRandom) {
            throw new IllegalArgumentException(ErrorMessages.BOUND_MUST_BE_GREATER);
        }
    }

    /**
     * Procesa concurrentemente las particiones de números usando un pool de hilos,
     * recolecta los resultados y los ordena.
     *
     * @param partitions Lista de particiones de números.
     * @param workers Número de hilos a utilizar.
     * @return Lista de resultados procesados y ordenados.
     * @throws InterruptedException Si la ejecución es interrumpida.
     */
    private List<Long> processPartitions(List<List<Long>> partitions, int workers) throws InterruptedException {
        ExecutorService workerPool = Executors.newFixedThreadPool(workers);
        List<Future<List<Long>>> futures = new ArrayList<>();
        try {
            for (List<Long> partition : partitions) {
                futures.add(workerPool.submit(() -> partition.stream()
                        .map(worker::processSquareNumber)
                        .collect(Collectors.toList())));
            }
            List<Long> allResults = new ArrayList<>();
            for (Future<List<Long>> future : futures) {
                allResults.addAll(future.get());
            }
            return sink.collectAndSortNumbers(allResults);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            workerPool.shutdown();
            workerPool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

}