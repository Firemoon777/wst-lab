package org.example.wst.standalone;

public class CatServiceFault {
    private static final String DEFAULT_MESSAGE = "Ошибка неизвестного характера.";
    protected String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static CatServiceFault defaultInstance() {
        CatServiceFault fault = new CatServiceFault();
        fault.message = DEFAULT_MESSAGE;
        return fault;
    }
}
