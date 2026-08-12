package creational.abstract_factory.solved.gcp;


import creational.abstract_factory.solved.ResourceFactory;
import creational.abstract_factory.solved.family.Instance;
import creational.abstract_factory.solved.family.Storage;

//Factory implementation for Google cloud platform resources
public class GoogleResourceFactory implements ResourceFactory {

	@Override
	public Instance createInstance(Instance.Capacity capacity) {
		return new GoogleComputeEngineInstance(capacity);
	}

	@Override
	public Storage createStorage(int capMib) {
		return new GoogleCloudStorage(capMib);
	}
	

}
