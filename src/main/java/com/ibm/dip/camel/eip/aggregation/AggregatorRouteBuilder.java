package com.ibm.dip.camel.eip.aggregation;

import com.ibm.dip.camel.eip.config.ApplicationConfig;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public class AggregatorRouteBuilder extends RouteBuilder
{

    private static final String EXPENSE_PROCESSING_ROUTE_ID = "expenseFileProcessingRoute";
    private static final String INCOME_PROCESSING_ROUTE_ID = "incomeFileProcessingRoute";
    private static final String ROUTE_AGGREGATOR_ROUTE_ID = "aggregateRoutes";

    @Autowired
    ApplicationConfig config;

    @Autowired
    CamelContext context;

    @Autowired
    FileLinesBatchingAggregatorStrategy fileLinesBatchingAggregatorStrategy;

    @Autowired
    @Qualifier("routeAggregatorStrategy")
    RouteAggregatorStrategy routeAggregatorStrategy;

    private ExecutorService buildExecutorService(ApplicationConfig.RouteConfig rc) throws Exception
    {
        if (rc.getExecutorConfig() == null)
        {
            return null;
        }

        return new ThreadPoolBuilder(context)
                .poolSize(rc.getExecutorConfig().getSize())
                .maxPoolSize(rc.getExecutorConfig().getMaxSize())
                .maxQueueSize(rc.getExecutorConfig().getQueueSize())
                .build(rc.getExecutorConfig().getName());
    }

    private void buildExpensesFileProcessingRoute() throws Exception
    {
        ApplicationConfig.RouteConfig rc = config.fetchRouteConfig(EXPENSE_PROCESSING_ROUTE_ID);
        ExecutorService es = buildExecutorService(rc);
        ApplicationConfig.CustomProperties batchSize = rc.fetchCustomProperties("batchSize");

        fileLinesBatchingAggregatorStrategy.setBatchSize(Integer.parseInt(batchSize.getValue()));

        from(rc.getInputEndpoints())
                .routeId(rc.getName())
                .autoStartup(rc.isAutoStartup())
                .split(body().tokenize("\n"))
                .executorService(es)
                .parallelProcessing()
                .aggregate(constant(true), fileLinesBatchingAggregatorStrategy)
                .completionSize(100)
                .completionPredicate(fileLinesBatchingAggregatorStrategy)
                .process("expenseAggregator")
                .end()
                .end()
                .setHeader("correlation-id", () -> "123")
                .process("expenseAggregationCompletionProcessor")
                .to(rc.getOutputEndpoints())
                .end();
    }

    private void buildIncomeFileProcessingRoute() throws Exception
    {
        ApplicationConfig.RouteConfig rc = config.fetchRouteConfig(INCOME_PROCESSING_ROUTE_ID);
        ExecutorService es = buildExecutorService(rc);
        ApplicationConfig.CustomProperties batchSize = rc.fetchCustomProperties("batchSize");

        fileLinesBatchingAggregatorStrategy.setBatchSize(Integer.parseInt(batchSize.getValue()));

        from(rc.getInputEndpoints())
                .routeId(rc.getName())
                .autoStartup(rc.isAutoStartup())
                .split(body().tokenize("\n"))
                .executorService(es)
                .parallelProcessing()
                .aggregate(constant(true), fileLinesBatchingAggregatorStrategy)
                .completionSize(100)
                .completionPredicate(fileLinesBatchingAggregatorStrategy)
                .process("incomeAggregator")
                .end()
                .end()
                .setHeader("correlation-id", () -> "123")
                .process("incomeAggregationCompletionProcessor")
                .to(rc.getOutputEndpoints())
                .end();
    }

    private void buildRouteAggregatorRoute()
    {
        ApplicationConfig.RouteConfig rc = config.fetchRouteConfig(ROUTE_AGGREGATOR_ROUTE_ID);

        from(rc.getInputEndpoints())
                .routeId(rc.getName())
                .aggregate(header("correlation-id"), routeAggregatorStrategy)
                .ignoreInvalidCorrelationKeys()
                .process("aggregatedRoutesProcessor");
    }

//    private RouteDefinition buildTimedRoute2()
//    {
//        return from("timer://timedRoute2?delay=10000&fixedRate=60000&repeatCount=2")
//                .routeId("TimedRoute2")
//                .setHeader("correlation-id", () -> "123")
//                .process("TimedRoute2Processor")
//                .to("direct:aggregateRoute");
//    }
//
//    private RouteDefinition buildTimedRoute3()
//    {
//        return from("timer://timedRoute3?delay=20000&fixedRate=60000&repeatCount=2")
//                .routeId("TimedRoute3")
//                .setHeader("correlation-id", () -> "123")
//                .process("TimedRoute3Processor")
//                .to("direct:aggregateRoute");
//    }

    @Override
    public void configure() throws Exception
    {
        buildExpensesFileProcessingRoute();
        buildIncomeFileProcessingRoute();
        buildRouteAggregatorRoute();
//        buildTimedRoute2();
//        buildTimedRoute3();
    }
}
