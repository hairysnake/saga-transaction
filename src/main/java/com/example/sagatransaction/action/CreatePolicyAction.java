package com.example.sagatransaction.action;

import com.example.sagatransaction.aop.SagaTransaction;
import com.example.sagatransaction.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class CreatePolicyAction {

    private final PolicyService policyService;
    private final AtomicInteger policyNumber = new AtomicInteger(0);

    @SagaTransaction
    public void createPolicy(String policyDefinition) {
        policyService.createPolicy(" policy " + policyNumber.addAndGet(1));
    }
}
