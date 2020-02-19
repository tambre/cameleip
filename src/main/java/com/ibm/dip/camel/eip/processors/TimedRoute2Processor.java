package com.ibm.dip.camel.eip.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("TimedRoute2Processor")
public class TimedRoute2Processor implements Processor
{
    private static final Logger logger = LoggerFactory.getLogger(TimedRoute2Processor.class);

    @Override
    public void process(Exchange exchange) throws Exception
    {
        logger.debug("Processing TimedRoute2Event");
        exchange.getMessage().setBody("TimedRoute2");
    }
}
