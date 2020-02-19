package com.ibm.dip.camel.eip.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("AggregateProcessor")
public class AggregateProcessor implements Processor
{
    private static final Logger logger = LoggerFactory.getLogger(AggregateProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception
    {
        logger.debug("Processing Aggregated Event");
    }
}
