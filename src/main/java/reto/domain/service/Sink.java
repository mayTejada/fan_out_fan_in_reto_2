package reto.domain.service;

import java.util.List;

public interface Sink {
    List<Long> collectAndSortNumbers(List<Long> results);
}
