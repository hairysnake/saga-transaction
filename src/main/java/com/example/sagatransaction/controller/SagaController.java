package com.example.sagatransaction.controller;

import com.example.sagatransaction.action.CreatePolicyAction;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SagaController {

    private final CreatePolicyAction createPolicyAction;

    @GetMapping("/")
    public void runSagaTransaction() {
        new Thread(createPolicyAction::createPolicy).start();
        new Thread(createPolicyAction::createPolicy).start();
    }
}
