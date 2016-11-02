package coms6998.fall2016.managers;

public interface AdminDAO<T> {
	
	public boolean create(T entity) throws DAOException;
	
	public T read(String key) throws DAOException;
	
	public boolean update(String key, T entity) throws DAOException;
	
	public boolean delete(String key) throws DAOException;

	public class DAOException extends Exception {
	    public DAOException(String message) {
	        super(message);
	    }
	}

}
