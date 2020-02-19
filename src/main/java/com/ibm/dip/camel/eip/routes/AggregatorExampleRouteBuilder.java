package com.ibm.dip.camel.eip.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AggregatorExampleRouteBuilder extends RouteBuilder
{
    private RouteDefinition buildTimedRoute1()
    {
        return from("timer://timedRoute1?delay=5000&fixedRate=60000&repeatCount=2")
                .routeId("TimedRoute1")
                .setHeader("correlation-id", () -> "123")
                .process("TimedRoute1Processor")
                .to("direct:aggregateRoute");
    }

    private RouteDefinition buildTimedRoute2()
    {
        return from("timer://timedRoute2?delay=10000&fixedRate=60000&repeatCount=2")
                .routeId("TimedRoute2")
                .setHeader("correlation-id", () -> "123")
                .process("TimedRoute2Processor")
                .to("direct:aggregateRoute");
    }

    private RouteDefinition buildTimedRoute3()
    {
        return from("timer://timedRoute3?delay=20000&fixedRate=60000&repeatCount=2")
                .routeId("TimedRoute3")
                .setHeader("correlation-id", () -> "123")
                .process("TimedRoute3Processor")
                .to("direct:aggregateRoute");
    }

    @Override
    public void configure() throws Exception
    {
        buildTimedRoute1();
        buildTimedRoute2();
        buildTimedRoute3();

        from("direct:aggregateRoute")
                .routeId("aggregatorRoute")
                .aggregate(header("correlation-id"), new CustomAggregationStrategy())
                .ignoreInvalidCorrelationKeys()
                .process("AggregateProcessor");
    }

    public static class CustomAggregationStrategy implements AggregationStrategy, Predicate
    {

        @Override
        public boolean matches(Exchange exchange)
        {
            logger.debug("exchange passed to predicate: {}", exchange);

            if (exchange.getMessage().getBody() != null  && checkCompletion(exchange.getMessage().getBody(Set.class)))
            {
                return true;
            }
            return false;
        }

        private boolean checkCompletion(Set<String> msgs)
        {
            if (CollectionUtils.isNotEmpty(msgs) && msgs.contains("TimedRoute1") && msgs.contains("TimedRoute2") && msgs.contains("TimedRoute3"))
            {
                return true;
            }
            return false;

        }

        private static final Logger logger = LoggerFactory.getLogger(CustomAggregationStrategy.class);

        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange)
        {
            if (oldExchange == null)
            {
                return newExchange;
            }

            if (oldExchange.getMessage().getBody() == null)
            {
                oldExchange.getIn().setBody(new HashSet<>());
            }
            else if (oldExchange.getMessage().getBody() instanceof String)
            {
                String val = oldExchange.getMessage().getBody(String.class);
                Set<String> msgs = new HashSet<>();
                msgs.add(val);
                oldExchange.getMessage().setBody(msgs);
            }
            Set<String> msgs = (Set<String>)oldExchange.getIn().getBody();
            msgs.add(newExchange.getMessage().getBody(String.class));
            oldExchange.getMessage().setBody(msgs);

            if (checkCompletion(msgs))
            {
                oldExchange.setProperty(Exchange.AGGREGATION_COMPLETE_ALL_GROUPS, true);
                oldExchange.getMessage().setHeader(Exchange.AGGREGATION_COMPLETE_ALL_GROUPS, true);
            }
            logger.debug("aggregated output: {}", oldExchange.getMessage().getBody());
            return oldExchange;
        }
    }
}
