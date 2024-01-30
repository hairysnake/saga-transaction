package com.example.sagatransaction.service;

import com.example.sagatransaction.aop.SagaMethod;
import com.example.sagatransaction.entity.Policy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class PolicyService {

    @SagaMethod(rollbackMethod = "initializePolicyIdentifierRollback")
    public UUID initializePolicyIdentifier(int policyNumber) {
        log.info("Initializing policy identifier " + policyNumber);
        return UUID.randomUUID();
    }

    @SagaMethod(rollbackMethod = "createPolicyRollback")
    public Policy createPolicy(UUID id) {
        log.info("Creating policy " + id);
        return new Policy(id);
    }

    @SagaMethod(rollbackMethod = "throwExceptionAfterCreationRollback")
    public void throwExceptionAfterCreation(Policy policy) {
        throw new RuntimeException("Throwing exception for policy " + policy.id());
    }

    public void initializePolicyIdentifierRollback(UUID id) {
        log.info("Rollback - Removal of initialized policy identifier {}", id);
    }

    public void createPolicyRollback(Policy policy) {
        log.info("Rollback - Deletion policy {}", policy.id());
    }

    public void createPolicyRollback() {
        log.info("Rollback - Shouldn't be printed");
    }
}
