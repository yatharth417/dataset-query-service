package com.assignment.dataset.strategy;

public interface DatasetQueryStrategy {
    boolean supports(QueryContext context);
    Object execute(QueryContext context);
}