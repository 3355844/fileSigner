package fileSigner.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SignProcess {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long signId;

	@Column
	private String fileName;

	@Column
	private String privateKeyName;

	@Column
	private String publicKeyName;

	@Column
	private byte[] fileData;

	@Column
	private byte[] publicKeyData;

	@Column
	private byte[] privateKeyData;

	@Column
	private byte[] signature;

	@Column
	private String signName;

	public String getPublicKeyName() {
		return publicKeyName;
	}

	public void setPublicKeyName(String publicKeyName) {
		this.publicKeyName = publicKeyName;
	}

	public byte[] getPublicKeyData() {
		return publicKeyData;
	}

	public void setPublicKeyData(byte[] pulicKeyData) {
		this.publicKeyData = pulicKeyData;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public byte[] getPrivateKeyData() {
		return privateKeyData;
	}

	public void setPrivateKeyData(byte[] privateKeyData) {
		this.privateKeyData = privateKeyData;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getPrivateKeyName() {
		return privateKeyName;
	}

	public void setPrivateKeyName(String privateKeyName) {
		this.privateKeyName = privateKeyName;
	}

	public Long getSignId() {
		return signId;
	}

	public void setSignId(long signId) {
		this.signId = signId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
