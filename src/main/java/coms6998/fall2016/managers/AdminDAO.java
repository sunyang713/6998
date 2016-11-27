package coms6998.fall2016.managers;

public interface AdminDAO<T> {

	public DAOResponse<T> create(T entity);
	
	public DAOResponse<T> read(String key);
	
	public DAOResponse<T> update(String key, T entity);
	
	public DAOResponse<T> delete(String key);

	public class DAOResponse<S> {
		
		private boolean succeeded;
		private String message;
		private S payload;
		
		public DAOResponse(boolean succeeded, String message, S payload) {
			this.setSucceeded(succeeded);
			this.setMessage(message);
			this.setPayload(payload);
		}

		public boolean succeeded() {
			return succeeded;
		}

		public void setSucceeded(boolean succeeded) {
			this.succeeded = succeeded;
		}
		
		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public S getPayload() {
			return payload;
		}

		public void setPayload(S payload) {
			this.payload = payload;
		}

	}
	
}
