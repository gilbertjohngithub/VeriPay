package com.berstek.veripay.data_access;



public class TestDA extends DA {

    public void writeToConsole1(Object str) {
        rootRef.child("test").push().setValue(str);
    }

}
