package creational.factory.simple_factory.solved;

public class Client {

	public static void main(String[] args) {
		Post post = PostFactory.createPost("news");
		System.out.println(post);

	}

}
