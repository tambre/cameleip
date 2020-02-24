package com.ibm.dip.camel.eip.aggregation;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("aggregatedRoutesProcessor")
public class AggregatedRoutesProcessor implements Processor
{
    private static final Logger logger = LoggerFactory.getLogger(AggregatedRoutesProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception
    {
        Map<String, Double> expenseSummary = (Map<String, Double>) exchange.getMessage().getHeader("EXPENSE_SUMMARY");
        Map<String, Double> incomeSummary = (Map<String, Double>) exchange.getMessage().getHeader("INCOME_SUMMARY");

        logger.debug("expense Summary: {}", expenseSummary);
        logger.debug("income Summary: {}", incomeSummary);

        double expense = 0;
        double income = 0;
        if (MapUtils.isNotEmpty(expenseSummary))
        {
            expense = expenseSummary.entrySet().stream().mapToDouble(entry -> entry.getValue()).sum();
            logger.debug("Total Expenses: {}", expense);
        }
        if (MapUtils.isNotEmpty(incomeSummary))
        {
            income = incomeSummary.entrySet().stream().mapToDouble(entry -> entry.getValue()).sum();
            logger.debug("Total Income: {}", income);
        }

        logger.debug("Cashflow: {}", (income - expense));

    }
}
