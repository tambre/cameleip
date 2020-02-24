package com.ibm.dip.camel.eip.aggregation;

import com.ibm.dip.camel.eip.model.ExpensesSummary;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("expenseAggregationCompletionProcessor")
public class ExpenseAggregationCompletionProcessor implements Processor
{
    private static final Logger logger = LoggerFactory.getLogger(ExpenseAggregationCompletionProcessor.class);

    @Autowired
    @Qualifier("expensesSummary")
    ExpensesSummary expensesSummary;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        exchange.getMessage().setHeader("EXPENSE_SUMMARY", expensesSummary.getExpensesCategoryBreakup());
        exchange.getMessage().setHeader("EXPENSE_PROCESSING_COMPLETE", true);
    }
}
