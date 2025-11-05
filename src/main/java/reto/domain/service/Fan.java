package reto.domain.service;

import java.util.List;

public interface Fan {
    List<List<Long>> distributeNumbers(List<Long> data, int workers);
}
