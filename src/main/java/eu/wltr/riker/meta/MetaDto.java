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

	public <T extends MetaEntry> T get(Class<T> cls) {
		String name = cls.getCanonicalName();
		return collection.findOne("{_class: #}", name).as(cls);

	}

	public <T extends MetaEntry> void update(T value) {
		String name = value.getClass().getCanonicalName();
		collection.update("{_class: #}", name).upsert().with(value);

	}

}
