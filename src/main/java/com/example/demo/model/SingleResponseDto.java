package com.example.demo.model;

public class SingleResponseDto<T> {

    private T data;

    public SingleResponseDto(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
