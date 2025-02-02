package request;

import java.io.Serializable;

public class Request<T> implements Serializable, IRequest<T> {
    private final Class<T> requestType;
 
    private T result;

    public Request(final Class<T> requestType)
    {
        this.requestType = requestType;
    }

    public Class<T> getRequestType() {
        return requestType;
    }

    @Override
    public T getCommand() {
        return result;
    }

    @Override
    public void setCommand(T result) {
        this.result = result;
    }
}
