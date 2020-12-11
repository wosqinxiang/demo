package com.ahdms.framework.feign.ribbon.predicate;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import org.springframework.cloud.consul.discovery.ConsulServer;

/**
 * 过滤服务
 *
 * @author Katrel.zhou
 */
public abstract class DiscoveryEnabledPredicate extends AbstractServerPredicate {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean apply(PredicateKey input) {
        return input != null
                && input.getServer() instanceof ConsulServer
                && apply((ConsulServer) input.getServer());
    }

    /**
     * Returns whether the specific {@link ConsulServer} matches this predicate.
     *
     * @param server the discovered server
     * @return whether the server matches the predicate
     */
    protected abstract boolean apply(ConsulServer server);
}
