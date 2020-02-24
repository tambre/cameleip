package com.ibm.dip.camel.eip.model;

import org.apache.commons.collections4.MapUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExpensesSummary implements Serializable
{
    Map<String, Double> expensesCategoryBreakup;

    public ExpensesSummary()
    {
        this.expensesCategoryBreakup = new HashMap<>();
    }


    public Map<String, Double> getExpensesCategoryBreakup()
    {
        return expensesCategoryBreakup;
    }

    public synchronized void addExpense(String category, Double amount)
    {
        this.expensesCategoryBreakup.merge(category, amount, Double::sum);
    }

    public synchronized void addExpenses(Map<String, Double> partialMap)
    {
        if (MapUtils.isNotEmpty(partialMap))
        {
            Set<Map.Entry<String, Double>> entrySet = partialMap.entrySet();
            for (Map.Entry<String, Double> stringDoubleEntry : entrySet)
            {
                this.expensesCategoryBreakup.merge(stringDoubleEntry.getKey(), stringDoubleEntry.getValue(),
                                                   Double::sum);
            }
        }
    }

    @Override
    public String toString()
    {
        return "ExpensesSummary{" +
                "expensesCategoryBreakup=" + expensesCategoryBreakup +
                '}';
    }
}
