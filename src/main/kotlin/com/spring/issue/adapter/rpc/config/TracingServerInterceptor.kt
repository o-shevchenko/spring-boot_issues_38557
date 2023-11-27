package com.spring.issue.adapter.rpc.config

import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.SpanBuilder
import io.opentelemetry.api.trace.SpanContext
import io.opentelemetry.api.trace.TraceFlags
import io.opentelemetry.api.trace.TraceState
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import io.opentelemetry.context.Scope
import org.springframework.stereotype.Component

@Component
class TracingServerInterceptor(private val tracer: Tracer) : ServerInterceptor {

    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val traceId = headers.get(Metadata.Key.of("traceId", Metadata.ASCII_STRING_MARSHALLER))
        val spanId = headers.get(Metadata.Key.of("spanId", Metadata.ASCII_STRING_MARSHALLER))

        val spanContext = createSpanContext(traceId, spanId)
        val spanBuilder: SpanBuilder = tracer.spanBuilder("test-span").setParent(spanContext)
        val span: Span = spanBuilder.startSpan()
        val scopedContext: Context = Context.current().with(span)

        val scope: Scope = scopedContext.makeCurrent()

        try {
            return next.startCall(call, headers)
        } finally {
            scope.close()
            span.end()
        }
    }

    private fun createSpanContext(traceId: String?, spanId: String?): Context {
        val spanContext = SpanContext.createFromRemoteParent(
            traceId ?: "",
            spanId ?: "",
            TraceFlags.getDefault(),
            TraceState.getDefault()
        )
        return Context.current().with(Span.wrap(spanContext))
    }
}
