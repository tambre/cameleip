package com.ibm.dip.camel.eip.aggregation;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("routeAggregatorStrategy")
public class RouteAggregatorStrategy implements AggregationStrategy, Predicate
{
    private static final Logger logger = LoggerFactory.getLogger(RouteAggregatorStrategy.class);

    @Override
    public boolean matches(Exchange exchange)
    {
        if (exchange.getMessage() != null && checkCompletion(exchange))
        {
            logger.debug("Route Level aggregation complete");
            return true;
        }
        return false;
    }

    private boolean checkCompletion(Exchange exchange)
    {
        Boolean expenseProcessingComplete = (Boolean) exchange.getMessage().getHeader("EXPENSE_PROCESSING_COMPLETE");
        Boolean incomeProcessingComplete = (Boolean) exchange.getMessage().getHeader("INCOME_PROCESSING_COMPLETE");
        if (Boolean.TRUE.equals(expenseProcessingComplete) && Boolean.TRUE.equals(incomeProcessingComplete))
        {
            return true;
        }
        return false;
    }

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange)
    {
        if (oldExchange == null)
        {
            return newExchange;
        }
        else
        {
            Boolean expenseProcessingComplete = (Boolean) newExchange.getMessage().getHeader(
                    "EXPENSE_PROCESSING_COMPLETE");
            Boolean incomeProcessingComplete = (Boolean) newExchange.getMessage().getHeader(
                    "INCOME_PROCESSING_COMPLETE");
            if (expenseProcessingComplete != null)
            {
                oldExchange.getMessage().setHeader("EXPENSE_PROCESSING_COMPLETE", expenseProcessingComplete);
                oldExchange.getMessage().setHeader("EXPENSE_SUMMARY",
                                                   newExchange.getMessage().getHeader("EXPENSE_SUMMARY"));
            }

            if (incomeProcessingComplete != null)
            {
                oldExchange.getMessage().setHeader("INCOME_PROCESSING_COMPLETE", expenseProcessingComplete);
                oldExchange.getMessage().setHeader("INCOME_SUMMARY",
                                                   newExchange.getMessage().getHeader("INCOME_SUMMARY"));
            }
        }
        if (checkCompletion(oldExchange))
        {
            oldExchange.setProperty(Exchange.AGGREGATION_COMPLETE_ALL_GROUPS, true);
            oldExchange.getMessage().setHeader(Exchange.AGGREGATION_COMPLETE_ALL_GROUPS, true);
        }
        logger.debug("Route Level aggregation in-progress");
        return oldExchange;
    }
}
