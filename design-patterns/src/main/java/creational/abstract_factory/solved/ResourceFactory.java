package creational.abstract_factory.solved;


import creational.abstract_factory.solved.family.Instance;
import creational.abstract_factory.solved.family.Storage;

//Abstract factory with methods defined for each object type.
public interface ResourceFactory {

	Instance createInstance(Instance.Capacity capacity);
	
	Storage createStorage(int capMib);
}
