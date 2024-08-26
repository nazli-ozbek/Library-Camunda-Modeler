package com.camunda.project.delegate;

import com.camunda.project.dto.AgreementRequest;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class AgreementRequestDelegate implements JavaDelegate {
    Expression serviceUrl;

    @Override
    public void execute(DelegateExecution execution) {
        long book_id = (Long) execution.getVariable("book_id");
        long user_id = (Long) execution.getVariable("user_id");
        Date borrow_date = (Date) execution.getVariable("borrow_date");
        Date return_date = (Date) execution.getVariable("return_date");


        AgreementRequest agreementRequest = new AgreementRequest();
        if(!ObjectUtils.isEmpty(book_id)){
        agreementRequest.setBook_id(book_id);}
        if(!ObjectUtils.isEmpty(user_id)){
        agreementRequest.setUser_id(user_id);}
        if(!ObjectUtils.isEmpty(borrow_date)){
        agreementRequest.setBorrowDate(borrow_date);}
        if(!ObjectUtils.isEmpty(return_date)){
        agreementRequest.setReturnDate(return_date);}
        System.out.println("Request created");
        if(!serviceUrl.getExpressionText().equals("request")) {

            final String uri = "http://localhost:8082/api/management/" + serviceUrl.getExpressionText();

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<AgreementRequest> requestEntity = new HttpEntity<>(agreementRequest);

            ResponseEntity<Map> response = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            Map<String, Object> variables = new HashMap<>();
            variables.putAll(response.getBody());

            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
                execution.setVariable(entry.getKey(), entry.getValue());
            }
        }
        }
    }

