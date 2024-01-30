package com.example.sagatransaction.aop;

import java.lang.reflect.Method;

public record SagaRollbackMethod(Class<?> clazz, String methodName, Object value) {
}
