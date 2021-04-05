package ru.gasevsky.jarsoft;

public class TestRollBackException extends RuntimeException {
    private String msg;

    TestRollBackException(String load) {
        super("", null, false, false);
        this.msg = load;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}

