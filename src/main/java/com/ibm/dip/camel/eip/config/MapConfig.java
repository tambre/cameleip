package com.ibm.dip.camel.eip.config;

import com.ibm.dip.camel.eip.model.ExpensesSummary;
import com.ibm.dip.camel.eip.model.IncomeSummary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapConfig
{

    @Bean("expensesSummary")
    ExpensesSummary expensesSummary()
    {
        return new ExpensesSummary();
    }

    @Bean("incomeSummary")
    IncomeSummary incomeSummary()
    {
        return new IncomeSummary();
    }

}
