package com.example.calleridapplication;
public interface WebServiceCallBack<T> {

    void OnSuccess(T result);
    void OnError(String error);
}