package com.coder4.lmsia.gracefulshutdown;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

/**
 * @author coder4
 */
public class GracefulStatusHealthIndicator implements HealthIndicator {

    private static final String GRACEFUL_STATUS_KEY = "graceful_status";

    private Logger LOG = LoggerFactory.getLogger(getClass());

    private Health health;

    public GracefulStatusHealthIndicator() {
        setReady(true);
    }

    public void setReady(boolean ready) {
        synchronized (this) {
            if (ready) {
                health = new Health.Builder().withDetail(GRACEFUL_STATUS_KEY, "graceful_status_up").up().build();
                LOG.info("graceful_status up");
            } else {
                health = new Health.Builder().withDetail(GRACEFUL_STATUS_KEY, "graceful_status_down").down().build();
                LOG.info("graceful_status down");
            }
        }
    }

    @Override
    public Health health() {
        return health;
    }
}