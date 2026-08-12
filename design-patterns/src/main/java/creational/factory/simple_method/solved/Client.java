package creational.factory.simple_method.solved;


import creational.factory.simple_method.solved.message.Message;

public class Client {

	public static void main(String[] args) {
		printMessage(new JSONMessageCreator());
		
		printMessage(new TextMessageCreator());
		MessageCreator messageCreator=new TextMessageCreator();
		System.out.println(messageCreator.getMessage().getContent());

		MessageCreator messageCreator1=new JSONMessageCreator();
		System.out.println(messageCreator1.getMessage().getContent());

	}
	
	public static void printMessage(MessageCreator creator) {
		Message msg = creator.getMessage();
		System.out.println(msg);
	}
}
