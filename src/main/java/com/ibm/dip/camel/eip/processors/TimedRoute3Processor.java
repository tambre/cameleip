package com.ibm.dip.camel.eip.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("TimedRoute3Processor")
public class TimedRoute3Processor implements Processor
{
    private static final Logger logger = LoggerFactory.getLogger(TimedRoute3Processor.class);

    @Override
    public void process(Exchange exchange) throws Exception
    {
        logger.debug("Processing TimedRoute3Event");
        exchange.getMessage().setBody("TimedRoute3");

    }
}
