package com.company.quizapp.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ResponseObject {
    private int id;
    private String response;
}
