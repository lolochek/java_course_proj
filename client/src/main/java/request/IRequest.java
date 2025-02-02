package request;

public interface IRequest<T> {
    public T getCommand();
    public void setCommand(T result);
}
