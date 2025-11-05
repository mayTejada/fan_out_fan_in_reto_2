package reto.service.test;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import reto.domain.service.Fan;
import reto.domain.service.Sink;
import reto.domain.service.WorkerInterface;
import reto.exception.ErrorMessages;
import reto.infrastructure.util.GeneratorRandom;
import reto.service.ResultsService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

public class ResultsServiceTest {
    private final Fan fan = mock(Fan.class);
    private final WorkerInterface worker = mock(WorkerInterface.class);
    private final Sink sink = mock(Sink.class);
    private final ResultsService resultsService = new ResultsService(fan, worker, sink);

    @Test
    public void resultsSquareNumbersWithValidNumbersList() throws InterruptedException {
        List<Long> numbers = Arrays.asList(1L, 2L, 3L);
        List<List<Long>> partitions = Collections.singletonList(numbers);
        List<Long> processedNumbers = Arrays.asList(1L, 4L, 9L);

        when(fan.distributeNumbers(eq(numbers), anyInt())).thenReturn(partitions);
        when(worker.processSquareNumber(anyLong())).thenAnswer(invocation -> {
            Long num = invocation.getArgument(0);
            return num * num;
        });
        when(sink.collectAndSortNumbers(anyList())).thenReturn(processedNumbers);

        List<Long> result = resultsService.resultsSquareNumbers(numbers, 0, 0, 0);

        assertEquals(processedNumbers, result);
        verify(fan).distributeNumbers(eq(numbers), anyInt());
        verify(worker, times(3)).processSquareNumber(anyLong());
        verify(sink).collectAndSortNumbers(anyList());
    }

    @Test
    public void resultsSquareNumbersGeneratesRandomNumbersWhenInputIsEmpty() throws InterruptedException {
        List<Long> generatedNumbers = Arrays.asList(5L, 10L, 15L);
        List<List<Long>> partitions = Collections.singletonList(generatedNumbers);
        List<Long> processedNumbers = Arrays.asList(25L, 100L, 225L);

        try (MockedStatic<GeneratorRandom> mockedStatic = mockStatic(GeneratorRandom.class)) {
            mockedStatic.when(() -> GeneratorRandom.generateRandomNumbers(3L, 1L, 20L)).thenReturn(generatedNumbers);

            when(fan.distributeNumbers(eq(generatedNumbers), anyInt())).thenReturn(partitions);
            when(worker.processSquareNumber(anyLong())).thenAnswer(invocation -> {
                Long num = invocation.getArgument(0);
                return num * num;
            });
            when(sink.collectAndSortNumbers(anyList())).thenReturn(processedNumbers);

            List<Long> result = resultsService.resultsSquareNumbers(Collections.emptyList(), 3, 1, 20);

            assertEquals(processedNumbers, result);
            verify(fan).distributeNumbers(eq(generatedNumbers), anyInt());
            verify(worker, times(3)).processSquareNumber(anyLong());
            verify(sink).collectAndSortNumbers(anyList());
        }
    }

    @Test
    public void resultsSquareNumbersThrowsExceptionForInvalidGenerationParams() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () ->
                resultsService.resultsSquareNumbers(Collections.emptyList(), 0, 1, 20)
        );
        assertEquals(ErrorMessages.SIZE_MUST_BE_POSITIVE, exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () ->
                resultsService.resultsSquareNumbers(Collections.emptyList(), 3, 20, 10)
        );
        assertEquals(ErrorMessages.BOUND_MUST_BE_GREATER, exception2.getMessage());
    }

    @Test
    public void resultsSquareNumbersProcessesAndSortsResultsCorrectly() throws InterruptedException {
        List<Long> numbers = Arrays.asList(3L, 1L, 2L);
        List<List<Long>> partitions = Arrays.asList(Collections.singletonList(3L), Collections.singletonList(1L), Collections.singletonList(2L));
        List<Long> processedNumbers = Arrays.asList(9L, 1L, 4L);
        List<Long> sortedNumbers = Arrays.asList(1L, 4L, 9L);

        when(fan.distributeNumbers(eq(numbers), anyInt())).thenReturn(partitions);
        when(worker.processSquareNumber(anyLong())).thenAnswer(invocation -> {
            Long num = invocation.getArgument(0);
            return num * num;
        });
        when(sink.collectAndSortNumbers(eq(processedNumbers))).thenReturn(sortedNumbers);

        List<Long> result = resultsService.resultsSquareNumbers(numbers, 0, 0, 0);

        assertEquals(sortedNumbers, result);
        verify(fan).distributeNumbers(eq(numbers), anyInt());
        verify(worker, times(3)).processSquareNumber(anyLong());
        verify(sink).collectAndSortNumbers(eq(processedNumbers));
    }
}