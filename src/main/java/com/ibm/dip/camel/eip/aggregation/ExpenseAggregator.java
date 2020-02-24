package com.ibm.dip.camel.eip.aggregation;

import com.ibm.dip.camel.eip.model.ExpensesSummary;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("expenseAggregator")
public class ExpenseAggregator implements Processor
{
    private static final Logger logger = LoggerFactory.getLogger(ExpenseAggregator.class);

    @Autowired
    @Qualifier("expensesSummary")
    ExpensesSummary expensesSummary;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        List<String> lines = (List<String>) exchange.getMessage().getBody();
        if (CollectionUtils.isNotEmpty(lines))
        {
            logger.debug("size of list: {}", lines.size());

            Map<String, Double> partialMap = lines.stream()
                    .map(line -> StringUtils.commaDelimitedListToStringArray(line))
                    .collect(Collectors.groupingBy(strings -> strings[1], Collectors.summingDouble(
                            strings -> Double.parseDouble(strings[3]))));
            expensesSummary.addExpenses(partialMap);

            logger.debug("Aggregated Expense Summary: {}", expensesSummary);
            exchange.getMessage().setHeader("aggregatedExpenses", expensesSummary);
        }
        else
        {
            logger.warn("empty list of lines passed");
        }
    }
}
