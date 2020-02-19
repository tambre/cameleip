package com.ibm.dip.camel.eip.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("TimedRoute1Processor")
public class TimedRoute1Processor implements Processor
{
    private static final Logger logger = LoggerFactory.getLogger(TimedRoute1Processor.class);

    @Override
    public void process(Exchange exchange) throws Exception
    {
        logger.debug("Processing TimedRoute1Event");
        exchange.getMessage().setBody("TimedRoute1");
    }
}
