package creational.abstract_factory.solved.aws;

import creational.abstract_factory.solved.ResourceFactory;
import creational.abstract_factory.solved.family.Instance;
import creational.abstract_factory.solved.family.Storage;

//Factory implementation for Google cloud platform resources
public class AwsResourceFactory implements ResourceFactory {

	@Override
	public Instance createInstance(Instance.Capacity capacity) {
		return new Ec2Instance(capacity);
	}

	@Override
	public Storage createStorage(int capMib) {
		return new S3Storage(capMib);
	}


}
