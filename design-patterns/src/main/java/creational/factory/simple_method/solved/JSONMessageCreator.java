package creational.factory.simple_method.solved;


import creational.factory.simple_method.solved.message.JSONMessage;
import creational.factory.simple_method.solved.message.Message;

/**
 * Provides implementation for creating JSON messages
 */
public class JSONMessageCreator extends MessageCreator {

	@Override
	public Message createMessage() {
		return new JSONMessage();
	}

	
}
