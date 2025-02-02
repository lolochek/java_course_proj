package db.dao;

import java.util.List;

public interface IDao<T> {
    public List<T> getAll();
    public boolean update(T obj);
    public boolean save(T obj);
    public boolean remove(T obj);
    public T getById(int id);
    public String getPassword(int id);
}
