package eu.wltr.riker.meta;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MetaBo {

	private final MetaDto metaDto;

	@Autowired
	public MetaBo(MetaDto metaDto) {
		this.metaDto = metaDto;

	}

	public Sequence nextSequence() {
		Sequence seq = metaDto.get(Sequence.class);
		if (seq == null)
			seq = new Sequence();
		seq.increment();
		metaDto.update(seq);

		return seq;

	}

	public <T> T get(Class<T> cls) {
		return metaDto.get(cls);

	}

}
