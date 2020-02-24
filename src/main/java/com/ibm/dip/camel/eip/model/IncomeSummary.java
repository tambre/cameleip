package com.ibm.dip.camel.eip.model;

import org.apache.commons.collections4.MapUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IncomeSummary implements Serializable
{
    Map<String, Double> incomeCategoryBreakup;

    public IncomeSummary()
    {
        this.incomeCategoryBreakup = new HashMap<>();
    }


    public Map<String, Double> getIncomeCategoryBreakup()
    {
        return incomeCategoryBreakup;
    }

    public synchronized void addIncome(String category, Double amount)
    {
        this.incomeCategoryBreakup.merge(category, amount, Double::sum);
    }

    public synchronized void addIncome(Map<String, Double> partialMap)
    {
        if (MapUtils.isNotEmpty(partialMap))
        {
            Set<Map.Entry<String, Double>> entrySet = partialMap.entrySet();
            for (Map.Entry<String, Double> stringDoubleEntry : entrySet)
            {
                this.incomeCategoryBreakup.merge(stringDoubleEntry.getKey(), stringDoubleEntry.getValue(), Double::sum);
            }
        }
    }

    @Override
    public String toString()
    {
        return "ExpensesSummary{" +
                "expensesCategoryBreakup=" + incomeCategoryBreakup +
                '}';
    }
}
