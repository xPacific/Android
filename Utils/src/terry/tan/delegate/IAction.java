package terry.tan.delegate;

public interface IAction<T> {
	void invoke(T args);
}
