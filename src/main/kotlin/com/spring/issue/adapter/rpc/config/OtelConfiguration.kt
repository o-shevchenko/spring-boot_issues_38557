package com.spring.issue.adapter.rpc.config

import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import org.springframework.boot.actuate.autoconfigure.tracing.otlp.OtlpProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(OtlpProperties::class)
class OtelConfiguration {
    @Bean
    fun otlpExporter(properties: OtlpProperties): OtlpGrpcSpanExporter {
        val builder = OtlpGrpcSpanExporter.builder().setEndpoint(properties.endpoint)
        return builder.build()
    }

    @Bean
    fun tracer(otlpExporter: OtlpGrpcSpanExporter): Tracer {
        val tracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(BatchSpanProcessor.builder(otlpExporter).build())
            .build()

        val openTelemetry = OpenTelemetrySdk.builder()
            .setTracerProvider(tracerProvider)
            .buildAndRegisterGlobal()

        return openTelemetry.tracerProvider.get("test")
    }
}
