package com.ibm.dip.camel.eip.aggregation;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component("fileLinesBatchingAggregatorStrategy")
public class FileLinesBatchingAggregatorStrategy implements AggregationStrategy, Predicate
{
    private static final Logger logger = LoggerFactory.getLogger(FileLinesBatchingAggregatorStrategy.class);

    private int batchSize;

    public int getBatchSize()
    {
        return batchSize;
    }

    public void setBatchSize(int batchSize)
    {
        this.batchSize = batchSize;
    }

    @Override
    public boolean matches(Exchange exchange)
    {
        int splitIndex = (Integer) exchange.getProperty(Exchange.SPLIT_INDEX);
        boolean splitComplete = (Boolean) exchange.getProperty(Exchange.SPLIT_COMPLETE);
        if (splitComplete)
        {
            logger.debug("Split is complete: splitIndex = {} and splitComplete = {}", splitIndex, splitComplete);
            return true;
        }
        return false;
    }

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange)
    {
        String toTokenize = newExchange.getMessage().getBody(String.class);
        List<String> lines = null;
        if (oldExchange == null)
        {
            lines = new LinkedList<>();
            lines.add(toTokenize);
            oldExchange = newExchange;
            oldExchange.getMessage().setBody(lines);
        }
        else
        {
            lines = (List<String>) oldExchange.getMessage().getBody();
            lines.add(toTokenize);
            oldExchange.getMessage().setBody(lines);
            oldExchange.setProperty(Exchange.SPLIT_INDEX, newExchange.getProperty(Exchange.SPLIT_INDEX));
            oldExchange.setProperty(Exchange.SPLIT_COMPLETE, newExchange.getProperty(Exchange.SPLIT_COMPLETE));
            oldExchange.setProperty(Exchange.SPLIT_SIZE, newExchange.getProperty(Exchange.SPLIT_SIZE));
        }

        return oldExchange;
    }
}
