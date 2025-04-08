package io.jacob;

public class PlayerOneContestProvider implements ContestProvider {
    @Override
    public Contest create() {
        return new PlayerOneContest();
    }
}
