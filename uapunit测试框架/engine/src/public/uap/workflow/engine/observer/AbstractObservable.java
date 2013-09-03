package uap.workflow.engine.observer;

import java.util.Observable;

public abstract class AbstractObservable extends Observable {
	/**
	 * ����Ŀ����󣬸�Ŀ���������ͬ����
	 */
	public AbstractObservable() {
		this.addObserver(AsynDataBase.getInstance());
	}
	/**
	 * ͬ����ͬ������
	 */
	public void asyn() {
		setChanged();
		notifyObservers();
	}
	/**
	 * ͬ����ͬ������
	 */
	public void asyn(String args) {
		setChanged();
		notifyObservers(args);
	}
}
