package creational.factory.simple_method.solved;


import creational.factory.simple_method.solved.message.Message;
import creational.factory.simple_method.solved.message.TextMessage;

/**
 * Provides implementation for creating Text messages
 */
public class TextMessageCreator extends MessageCreator {

	@Override
	public Message createMessage() {
		return new TextMessage();
	}



}
