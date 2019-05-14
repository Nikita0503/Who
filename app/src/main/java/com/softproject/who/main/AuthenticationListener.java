package com.softproject.who.main;

public interface AuthenticationListener {
    void onCodeReceived(String authToken);
}
