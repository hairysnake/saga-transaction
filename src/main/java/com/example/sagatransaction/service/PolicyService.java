package com.example.sagatransaction.service;

import com.example.sagatransaction.aop.SagaMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class PolicyService {

    @SagaMethod(rollbackMethod = "rollbackPreparePolicy")
    public UUID preparePolicy() {
        log.info("Policy prepared");
        return UUID.randomUUID();
    }

    public void rollbackPreparePolicy(UUID uuid) {
        log.info("Rolling back the transaction " + uuid);
    }

    public void createPolicy(String someValue) {
        throw new RuntimeException("Some problem while creating policy " + someValue);
    }
}
