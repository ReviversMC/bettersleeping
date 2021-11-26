package com.github.reviversmc.bettersleeping.interfaces;

public interface SleepManaged{
    public int getNightsAwake();
    public void setNightsAwake(int nights);
    public void incrementNightsAwake(int amount);
}