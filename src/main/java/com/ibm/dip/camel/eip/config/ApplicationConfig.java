package com.ibm.dip.camel.eip.config;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component("applicationConfig")
@ConfigurationProperties(prefix = "application")
public class ApplicationConfig
{
    String name;
    List<RouteConfig> routeConfigs;

    public RouteConfig fetchRouteConfig(String name)
    {
        if (CollectionUtils.isNotEmpty(routeConfigs))
        {
            for (RouteConfig rc : routeConfigs)
            {
                if (StringUtils.equals(rc.getName(), name))
                {
                    return rc;
                }
            }
        }
        return null;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<RouteConfig> getRouteConfigs()
    {
        return routeConfigs;
    }

    public void setRouteConfigs(List<RouteConfig> routeConfigs)
    {
        this.routeConfigs = routeConfigs;
    }

    public static class RouteConfig
    {
        private String name;
        private boolean enabled;
        private boolean autoStartup;
        private String[] inputEndpoints;
        private String[] outputEndpoints;
        private Set<CustomProperties> customProperties;
        private ExecutorConfig executorConfig;

        public CustomProperties fetchCustomProperties(String key)
        {
            if (CollectionUtils.isNotEmpty(customProperties))
            {
                for (CustomProperties cp : customProperties)
                {
                    if (StringUtils.equals(cp.getKey(), key))
                    {
                        return cp;
                    }
                }
            }
            return null;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public boolean isEnabled()
        {
            return enabled;
        }

        public void setEnabled(boolean enabled)
        {
            this.enabled = enabled;
        }

        public boolean isAutoStartup()
        {
            return autoStartup;
        }

        public void setAutoStartup(boolean autoStartup)
        {
            this.autoStartup = autoStartup;
        }

        public String[] getInputEndpoints()
        {
            return inputEndpoints;
        }

        public void setInputEndpoints(String[] inputEndpoints)
        {
            this.inputEndpoints = inputEndpoints;
        }

        public String[] getOutputEndpoints()
        {
            return outputEndpoints;
        }

        public void setOutputEndpoints(String[] outputEndpoints)
        {
            this.outputEndpoints = outputEndpoints;
        }

        public Set<CustomProperties> getCustomProperties()
        {
            return customProperties;
        }

        public void setCustomProperties(
                Set<CustomProperties> customProperties)
        {
            this.customProperties = customProperties;
        }

        public ExecutorConfig getExecutorConfig()
        {
            return executorConfig;
        }

        public void setExecutorConfig(ExecutorConfig executorConfig)
        {
            this.executorConfig = executorConfig;
        }

        @Override
        public String toString()
        {
            return "RouteConfig{" +
                    "name='" + name + '\'' +
                    ", enabled=" + enabled +
                    ", autoStartup=" + autoStartup +
                    ", inputEndpoints=" + Arrays.toString(inputEndpoints) +
                    ", outputEndpoints=" + Arrays.toString(outputEndpoints) +
                    ", customProperties=" + customProperties +
                    ", executorConfig=" + executorConfig +
                    '}';
        }
    }

    public static class CustomProperties
    {
        private String key;
        private String value;

        public String getKey()
        {
            return key;
        }

        public void setKey(String key)
        {
            this.key = key;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return "CustomProperties{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public static class ExecutorConfig
    {
        private String name;
        private int size;
        private int maxSize;
        private int queueSize;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public int getSize()
        {
            return size;
        }

        public void setSize(int size)
        {
            this.size = size;
        }

        public int getMaxSize()
        {
            return maxSize;
        }

        public void setMaxSize(int maxSize)
        {
            this.maxSize = maxSize;
        }

        public int getQueueSize()
        {
            return queueSize;
        }

        public void setQueueSize(int queueSize)
        {
            this.queueSize = queueSize;
        }

        @Override
        public String toString()
        {
            return "ExecutorConfig{" +
                    "name='" + name + '\'' +
                    ", size=" + size +
                    ", maxSize=" + maxSize +
                    ", queueSize=" + queueSize +
                    '}';
        }
    }
}
