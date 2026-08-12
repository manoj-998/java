package creational.factory.simple_method.solved;


import creational.factory.simple_method.solved.message.Message;

/**
 * This is our abstract "creator". 
 * The abstract method createMessage() has to be implemented by
 * its subclasses.
 */
public abstract class MessageCreator {

	public Message getMessage() {
		Message msg = createMessage();

		msg.addDefaultHeaders();
		msg.encrypt();

		return msg;
	}

	
	//Factory method
	protected abstract Message createMessage();
}
