package com.example.sagatransaction.aop;

public record SagaRollbackMethod(Class<?> clazz, String methodName, Class<?> returnType, Object value) {
}
