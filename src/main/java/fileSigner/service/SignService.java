package fileSigner.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fileSigner.model.SignProcess;
import fileSigner.repository.SignProcessRepositroy;

@Named
public class SignService {

	private static final Logger logger = LoggerFactory.getLogger(SignService.class);

	@Inject
	private SignProcessRepositroy repository;

	public List<SignProcess> allSignes() {
		logger.info("begin allSignes()");
		List<SignProcess> signPocesses = repository.findAll();
		logger.info("list size: {}", signPocesses.size());
		return signPocesses;
	}
	
	public void save(SignProcess sign) {
		logger.info("Save sign with FileName {}", sign.getFileName());
		repository.save(sign);
	}
	
	public Optional<SignProcess> getSignById(Long id) {
		logger.info("getSignById {}", id);
		return repository.findById(id);
	}
}
