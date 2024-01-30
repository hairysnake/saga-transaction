package com.example.sagatransaction.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class SagaTransactionAspect {

    private final ApplicationContext applicationContext;
    private static final ThreadLocal<SagaContext> sagaContextHolder = new ThreadLocal<>();

    @Pointcut("@annotation(com.example.sagatransaction.aop.SagaTransaction)")
    public void sagaTransactionPointcut() { }

    @Pointcut("@annotation(com.example.sagatransaction.aop.SagaMethod) && execution(* *(..))")
    public void sagaMethodPointcut() { }

    @Before("sagaTransactionPointcut()")
    public void beforeSagaTransaction() {
        SagaContext ctx = new SagaContext();
        sagaContextHolder.set(new SagaContext());
    }

    @AfterReturning(pointcut = "sagaMethodPointcut()", returning = "result")
    public void afterReturningFromSagaMethod(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = ((MethodSignature) joinPoint.getSignature());
        Class<?> returnType = methodSignature.getReturnType();
        Class<?> clazz = methodSignature.getMethod().getDeclaringClass();
        String rollbackMethodName = methodSignature.getMethod().getAnnotation(SagaMethod.class).rollbackMethod();

        sagaContextHolder.get().getExecutionStack()
                .push(new SagaRollbackMethod(clazz, rollbackMethodName, returnType, result));
    }

    @After("sagaTransactionPointcut()")
    public void afterSagaTransaction() {
        sagaContextHolder.remove();
    }

    @AfterThrowing(pointcut = "sagaTransactionPointcut()", throwing = "error")
    public void afterThrowing(JoinPoint jp, Throwable error) {
        log.info("This is method run after throwing");

        try {
            Stack<SagaRollbackMethod> stack = sagaContextHolder.get().getExecutionStack();
            while(!stack.empty()) {
                executeRollbackSageMethod(stack.pop());
            }
        } catch (Throwable e) {
            log.error("Here one should persist information about unsuccessful transaction rollback ", e);
        }
    }

    private void executeRollbackSageMethod(SagaRollbackMethod sagaRollbackMethod) throws NoSuchMethodException
            , InvocationTargetException, IllegalAccessException {
        Method rollbackMethod = sagaRollbackMethod.clazz().getMethod(sagaRollbackMethod.methodName()
                , sagaRollbackMethod.returnType());
        Object bean = applicationContext.getBean(sagaRollbackMethod.clazz());
        rollbackMethod.invoke(bean, sagaRollbackMethod.value());
    }
}
