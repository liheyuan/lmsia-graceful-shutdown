package com.coder4.lmsia.gracefulshutdown.configuration;

import com.coder4.lmsia.gracefulshutdown.GracefulStatusHealthIndicator;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author coder4
 */
@Configuration
public class GracefulShutdownConfiguration implements DisposableBean {

    private static final int GRACE_SHUTDOWN_MS = 3000;

    private GracefulStatusHealthIndicator gshIndicator = new GracefulStatusHealthIndicator();

    @Bean
    @ConditionalOnMissingBean(GracefulStatusHealthIndicator.class)
    public GracefulStatusHealthIndicator gracefulStatusHealthIndicator() {
        return gshIndicator;
    }

    @Autowired
    @Qualifier("shutdownThriftServerRunnable")
    private Runnable shutdownThriftServerRunnable;

    @Override
    public void destroy() throws Exception {
        gshIndicator.setReady(false);
        Thread.sleep(GRACE_SHUTDOWN_MS);
        if (shutdownThriftServerRunnable != null) {
            shutdownThriftServerRunnable.run();
        }
    }
}