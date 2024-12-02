package com.sample.adapter;

public interface HelloWorldConnection {
    public String helloWorld();

    public String helloWorld(String name);

    public void close();
}