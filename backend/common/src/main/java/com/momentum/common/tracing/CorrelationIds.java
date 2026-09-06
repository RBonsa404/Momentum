package com.momentum.common.tracing;

public final class CorrelationIds {
    public static final String HTTP_HEADER = "X-Correlation-Id";
    public static final String USER_HEADER = "X-User-Id";
    public static final String AMQP_HEADER = "correlation-id";

    private CorrelationIds() {
    }
}
