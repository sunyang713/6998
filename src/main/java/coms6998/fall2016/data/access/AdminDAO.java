package coms6998.fall2016.data.access;

public interface AdminDAO {

	public DAOResponse create(Object entity);

	public DAOResponse read(String key);

	public DAOResponse update(String key, Object entity);

	public DAOResponse delete(String key);

}
