package com.spring.issue.adapter.rpc.config

import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class LogInterceptor : ServerInterceptor {

    private val logger = LoggerFactory.getLogger(javaClass)
    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>, headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        logger.info(call.methodDescriptor.fullMethodName)
        return next.startCall(call, headers)
    }
}
