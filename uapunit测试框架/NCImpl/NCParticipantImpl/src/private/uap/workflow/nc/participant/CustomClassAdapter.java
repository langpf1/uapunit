package uap.workflow.nc.participant;

import java.util.List;

import uap.workflow.app.participant.IParticipant;
import uap.workflow.app.participant.IParticipantAdapter;
import uap.workflow.app.participant.ParticipantContext;
import uap.workflow.app.participant.ParticipantException;

public class CustomClassAdapter implements IParticipantAdapter{

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findUsers(ParticipantContext context) throws ParticipantException
	{
		checkValidity(context);
		List<IParticipant> participants = context.getParticipants();
		IParticipant participant = participants.get(0);
		Object obj = participant.getProperty("CustomClass");
		try{
			Class<IParticipantAdapter> clazz;
			clazz = (Class<IParticipantAdapter>) Class.forName(obj.toString());
			IParticipantAdapter type = clazz.newInstance();
			return type.findUsers(context);
		} catch (ClassNotFoundException e) {
			throw new ParticipantException(e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new ParticipantException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
	}

	@Override
	public void checkValidity(ParticipantContext context) throws ParticipantException
	{
	}
}
