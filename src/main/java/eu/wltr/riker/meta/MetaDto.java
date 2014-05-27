package eu.wltr.riker.meta;


import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MetaDto {

	public static final String NAME = "meta";

	private final MongoCollection collection;

	@Autowired
	protected MetaDto(Jongo jongo) {
		this.collection = jongo.getCollection(NAME);

	}

	public <T> T get(Class<T> cls) {
		String id = cls.getCanonicalName();
		T value = collection.findOne("{_id: #}", id).as(cls);
		return value;

	}

	public <T> void update(T value) {
		String id = value.getClass().getCanonicalName();
		collection.update("{_id: #}", id).upsert().with(value);

	}

}
