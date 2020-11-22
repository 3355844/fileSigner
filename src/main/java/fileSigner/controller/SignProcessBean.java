package fileSigner.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fileSigner.model.SignProcess;
import fileSigner.service.SignService;

@Named
@ViewScoped
public class SignProcessBean {

	private static final Logger logger = LogManager.getLogger(SignProcessBean.class);

	private List<SignProcess> signProcesses;

	@Inject
	private SignService service;

	@PostConstruct
	private void init() {
		logger.info("begin init()");
		signProcesses = service.allSignes();
	}

	public void create() {
		logger.info("begin create()");
		signProcesses = service.allSignes();
	}

	public List<SignProcess> getSignProcesses() {
		return signProcesses;
	}

	public void setSignProcesses(List<SignProcess> signProcesses) {
		this.signProcesses = signProcesses;
	}

}
