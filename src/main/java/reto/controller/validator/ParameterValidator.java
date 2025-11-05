package reto.controller.validator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ParameterValidator {

    private List<Long> numbers;
    private Long rangeMaxGenerateRandom;
    private Long rangeMinGenerateRandom;
    private Long sizeListNumbers;

    public boolean isValid() {
        if (numbers == null || numbers.isEmpty()) {
            return rangeMaxGenerateRandom != null && rangeMinGenerateRandom != null && sizeListNumbers != null;
        }
        return true;
    }
}
