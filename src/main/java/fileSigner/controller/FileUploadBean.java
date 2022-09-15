package fileSigner.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.annotation.SessionScope;

import fileSigner.model.SignProcess;
import fileSigner.service.SignService;

@Named
@SessionScope
public class FileUploadBean {

	private static final Logger logger = LoggerFactory.getLogger(FileUploadBean.class);

	@Inject
	private SignService service;

	@PostConstruct
	private void init() {
		logger.info("begin init()");
		signProcesses = service.allSignes();
	}

	private UploadedFile file;
	private UploadedFile publicKey;
	private UploadedFile privateKey;
	private StreamedContent fileOut;
	private SignProcess selectedSignProcess;
	private List<SignProcess> signProcesses;

	public UploadedFile getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(UploadedFile publicKey) {
		this.publicKey = publicKey;
	}

	public StreamedContent getFileOut() {
		return fileOut;
	}

	public void setFileOut(StreamedContent fileOut) {
		this.fileOut = fileOut;
	}

	public SignProcess getSelectedSignProcess() {
		return selectedSignProcess;
	}

	public void setSelectedSignProcess(SignProcess selectedSignProcess) {
		this.selectedSignProcess = selectedSignProcess;
	}

	public List<SignProcess> getSignProcesses() {
		return signProcesses;
	}

	public void setSignProcesses(List<SignProcess> signProcesses) {
		this.signProcesses = signProcesses;
	}

	public UploadedFile getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(UploadedFile privateKey) {
		this.privateKey = privateKey;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void fileSelection(String fileName, String signId) {
		logger.info("File selected :" + fileName);
		Optional<SignProcess> sign = service.getSignById(Long.valueOf(signId));
		fileOut = DefaultStreamedContent.builder().contentType("application/octet-stream").name(fileName)
				.stream(() -> new ByteArrayInputStream(sign.get().getFileData())).build();
	}

	public void upload() {
		logger.info("Begin upload file for signing");
		if (null != file && null != privateKey && publicKey != null && file.getSize() != 0 && privateKey.getSize() != 0
				&& publicKey.getSize() != 0) {
			createSign(file, privateKey, publicKey);
			FacesMessage message = new FacesMessage("Successful ", file.getFileName() + " is uploaded");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else if (privateKey == null && file.getSize() == 0) {
			FacesMessage message = new FacesMessage("Error", "Please, choose file and pivateKey");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else if (privateKey == null) {
			FacesMessage message = new FacesMessage("Error",
					"privateKey is empty or wrong format, privateKey has '.key' extention ");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else if (publicKey == null) {
			FacesMessage message = new FacesMessage("Error",
					"publicKey is empty or wrong format, publicKey has '.pub' extention ");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else {
			FacesMessage message = new FacesMessage("Error", "file not uploading");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	private void createSign(UploadedFile file, UploadedFile privateKey, UploadedFile publicKey) {
		logger.info("begin create sign");
		SignProcess sign = new SignProcess();
		sign.setFileName(file.getFileName());
		sign.setPrivateKeyName(privateKey.getFileName());
		sign.setPublicKeyName(publicKey.getFileName());
		try {
			logger.info("getting data from file");
			sign.setFileData(file.getInputStream().readAllBytes());
			logger.info("getting data from privateKey");
			sign.setPrivateKeyData(privateKey.getInputStream().readAllBytes());
			logger.info("getting data from pulicKey");
			sign.setPublicKeyData(publicKey.getInputStream().readAllBytes());
		} catch (IOException e) {
			logger.error("Error get data from files ");
		}
		service.save(sign);
		signProcesses = service.allSignes();
	}
}
