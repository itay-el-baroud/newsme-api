package com.medianote.app.core;
public class StateManager {
    public enum State { IDLE, LOADING, SUCCESS, ERROR, NO_INTERNET }
    public interface Listener { void onStateChanged(State state, String message); }
}
