package com.example.madtlab5;

public class CurrencyRate {
    private String code;
    private double rate;

    public CurrencyRate(String code, double rate) {
        this.code = code;
        this.rate = rate;
    }

    public String getCode() {
        return code;
    }

    public double getRate() {
        return rate;
    }
}

