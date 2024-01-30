package com.example.sagatransaction.aop;

import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

@Getter
@Setter
public class SagaContext {
    private Stack<SagaRollbackMethod> executionStack = new Stack<>();
}
