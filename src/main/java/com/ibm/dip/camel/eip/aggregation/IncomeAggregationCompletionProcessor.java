package com.ibm.dip.camel.eip.aggregation;

import com.ibm.dip.camel.eip.model.IncomeSummary;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("incomeAggregationCompletionProcessor")
public class IncomeAggregationCompletionProcessor implements Processor
{
    private static final Logger logger = LoggerFactory.getLogger(IncomeAggregationCompletionProcessor.class);

    @Autowired
    @Qualifier("incomeSummary")
    IncomeSummary incomeSummary;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        exchange.getMessage().setHeader("INCOME_SUMMARY", incomeSummary.getIncomeCategoryBreakup());
        exchange.getMessage().setHeader("INCOME_PROCESSING_COMPLETE", true);
    }
}
