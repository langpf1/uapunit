package uap.workflow.engine.bridge;
public interface IBridge<T, M> {
	T convertT2M(M object);
	M convertM2T(T object);
}
