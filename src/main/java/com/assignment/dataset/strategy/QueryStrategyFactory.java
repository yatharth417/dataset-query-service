package com.assignment.dataset.strategy;

import com.assignment.dataset.exception.InvalidQueryParameterException;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class QueryStrategyFactory {

    private final List<DatasetQueryStrategy> strategies;

    public QueryStrategyFactory(List<DatasetQueryStrategy> strategies) {
        this.strategies = strategies;
    }

    public DatasetQueryStrategy getStrategy(QueryContext context) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(context))
                .findFirst()
                .orElseThrow(() -> new InvalidQueryParameterException(
                        "Either 'groupBy' or 'sortBy' query parameter must be provided."
                ));
    }
}