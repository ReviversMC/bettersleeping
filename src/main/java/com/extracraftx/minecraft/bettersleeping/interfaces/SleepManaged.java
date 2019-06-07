package com.extracraftx.minecraft.bettersleeping.interfaces;

public interface SleepManaged{
    public int getNightsAwake();
    public void setNightsAwake(int nights);
    public void incrementNightsAwake(int amount);
}