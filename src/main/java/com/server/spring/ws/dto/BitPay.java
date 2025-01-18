package com.server.spring.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BitPay {
    private String code;
    private String name;
    private double rate;
}
