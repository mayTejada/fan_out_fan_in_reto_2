package reto.controller;

import reto.controller.validator.ParameterValidator;
import reto.service.ResultsService;
import reto.exception.ErrorMessages;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ResultsSquareController {
    private final ResultsService resultsService;

    public ResultsSquareController(ResultsService resultsService) {
        this.resultsService = resultsService;
    }

    @PostMapping("/square")
    public ResponseEntity<?> resultsSquare(@RequestBody ParameterValidator params) throws InterruptedException {
        if (!params.isValid()) {
            return ResponseEntity.badRequest()
                    .body(ErrorMessages.INSUFFICIENT_PARAMS);
        }
        List<Long> result = resultsService.resultsSquareNumbers(
                params.getNumbers(),
                params.getSizeListNumbers() != null ? params.getSizeListNumbers() : 0L,
                params.getRangeMinGenerateRandom() != null ? params.getRangeMinGenerateRandom() : 0L,
                params.getRangeMaxGenerateRandom() != null ? params.getRangeMaxGenerateRandom() : 0L
        );
        return ResponseEntity.ok(result);
    }
}
