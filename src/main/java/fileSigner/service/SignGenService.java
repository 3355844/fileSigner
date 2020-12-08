package fileSigner.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.web.context.annotation.SessionScope;

import fileSigner.model.SignProcess;

@Named
@SessionScope
public class SignGenService {

	private static final Logger logger = LogManager.getLogger(SignGenService.class);
	private static final String SIGNING_ALGORITHM = "SHA256withRSA";
	private static final String RSA = "RSA";
	private KeyFactory keyFactory;
	private KeyPairGenerator keyPairGenerator;
	private byte[] signatureBytes;
	private StreamedContent signOut;
	private StreamedContent keyPairZip;

	public StreamedContent getKeyPairZip() {
		return keyPairZip;
	}

	public void setKeyPairZip(StreamedContent keyPairZip) {
		this.keyPairZip = keyPairZip;
	}

	public StreamedContent getSignOut() {
		return signOut;
	}

	public void setSignOut(StreamedContent signOut) {
		this.signOut = signOut;
	}

	@Inject
	private SignService service;

	@PostConstruct
	public void init() {
		try {
			keyFactory = KeyFactory.getInstance(RSA);
			keyPairGenerator = KeyPairGenerator.getInstance(RSA);
			keyPairGenerator.initialize(2048);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

	public void generateKeyPair() {
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		Key pub = keyPair.getPublic();
		Key pvt = keyPair.getPrivate();
		keyPairZip = DefaultStreamedContent.builder().contentType("application/octet-stream").name("keyPair.zip")
				.stream(() -> new ByteArrayInputStream(zipKeys(pub, pvt))).build();
	}

	private byte[] zipKeys(Key pub, Key pvt) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		ZipEntry pubKey = new ZipEntry("PublicKey.pub");
		pubKey.setSize(pub.getEncoded().length);
		ZipEntry pvtKey = new ZipEntry("PrivateKey.key");
		pvtKey.setSize(pvt.getEncoded().length);
		try {
			zos.putNextEntry(pubKey);
			zos.write(pub.getEncoded());
			zos.closeEntry();
			zos.putNextEntry(pvtKey);
			zos.write(pvt.getEncoded());
			zos.closeEntry();
			zos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	private byte[] getArchiveForSending(SignProcess signProcess) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		ZipEntry file = new ZipEntry(signProcess.getFileName());
		ZipEntry sign = new ZipEntry(signProcess.getSignName());
		ZipEntry publicKey = new ZipEntry(signProcess.getPublicKeyName());
		try {
			zos.putNextEntry(file);
			zos.write(signProcess.getFileData());
			zos.closeEntry();
			zos.putNextEntry(sign);
			zos.write(signProcess.getSignature());
			zos.closeEntry();
			zos.putNextEntry(publicKey);
			zos.write(signProcess.getPublicKeyData());
			zos.closeEntry();
			zos.close();
		} catch (Exception e) {
			logger.error("Error get Archive For Sending: ", e);
		}
		return baos.toByteArray();
	}

	public void createDigitSignature(String signId) {
		Optional<SignProcess> signProcessResult = service.getSignById(Long.valueOf(signId));
		SignProcess signProcess = signProcessResult.get();
		String name = "";
		try {
			name += signProcess.getFileName().substring(0, signProcess.getFileName().lastIndexOf('.'));
			Signature signature = Signature.getInstance(SIGNING_ALGORITHM);
			PrivateKey privKey = getPrivateKey(signProcess.getPrivateKeyData());
			signature.initSign(privKey);
			signature.update(signProcess.getFileData());
			signatureBytes = signature.sign();
			signProcess.setSignature(signatureBytes);
			signProcess.setSignName("sign_" + name + ".txt");
			service.save(signProcess);
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			e.printStackTrace();
		}
		signOut = DefaultStreamedContent.builder().contentType("application/octet-stream").name(name + ".zip" )
				.stream(() -> new ByteArrayInputStream(getArchiveForSending(signProcess))).build();
	}

	private PrivateKey getPrivateKey(byte[] signData) {
		logger.info("get private key by sign lenght: " + signData.length);
		PKCS8EncodedKeySpec spec;
		PrivateKey privKey = null;
		spec = new PKCS8EncodedKeySpec(signData);
		try {
			privKey = keyFactory.generatePrivate(spec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return privKey;
	}
}
