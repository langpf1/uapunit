package uap.workflow.app.participant;

import uap.workflow.app.config.ParticipantTypeFactory;

public class ParticipantFactory{

	/**
	 * µ¥Àý
	 */
	private static ParticipantFactory inst = new ParticipantFactory();

	public static ParticipantFactory getInstance() {
		return inst;
	}

	public IParticipantAdapter getParticipantAdapter(IParticipantType participantType) {
		IParticipantAdapter pa = null;
		ParticipantTypeFactory typeFactory = ParticipantTypeFactory.getInstance();
		String implClassName = typeFactory.getImpl(participantType.getCode());
		
		try {
			Class<IParticipantAdapter> clazz;
			clazz = (Class<IParticipantAdapter>) Class.forName(implClassName);
			pa = clazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new ParticipantException(e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new ParticipantException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		return pa;
	}
}
