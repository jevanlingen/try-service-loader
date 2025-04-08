package io.jacob;

public class PlayerOneContest implements Contest {
    @Override
    public int highestNumber(int a, int b) {
        return Math.max(a, b);
    }

    @Override
    public String removeDuplicates(String s) {
        return "";
    }
}
