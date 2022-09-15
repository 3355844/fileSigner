package fileSigner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import javax.inject.Inject;

import org.assertj.core.util.Strings;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import fileSigner.model.SignProcess;
import fileSigner.service.SignGenService;
import fileSigner.service.SignService;

@SpringBootTest
public class SignServiceTest {

	private static final String TYPE = "application/octet-stream";
	private static final String SIGN_ID_SIGNED = "3";
	private static final String SIGN_ID_UNSIGNED = "2";
	private static final Logger logger = LoggerFactory.getLogger(SignServiceTest.class);
	private static SignProcess signProc;

	@Inject
	SignGenService signGenServ;

	@Inject
	SignService service;

	@BeforeAll
	static void setup() {
		logger.info("==================LOADING BEGIN=====================");
	}

	@AfterAll
	static void done() {
		logger.info("=================SHUTDOWN FINISHED==================");
	}

	@Test
	void generateKeyPairTest() {
		logger.info("****************************************************");
		logger.info("*************Begin generateKeyPairTest**************");
		logger.info("****************************************************");
		logger.info("");
		assertTrue(notEmptyGeneratedKeyPairZipContent());
		assertTrue(filesForVeryfyTestIsPresent(SIGN_ID_SIGNED));
		assertTrue(filesVerifyed());
		assertTrue(filesForVeryfyTestIsPresent(SIGN_ID_UNSIGNED));
		assertFalse(filesVerifyed());
		logger.info("");
		logger.info("****************************************************");
		logger.info("************Finish generateKeyPairTest**************");
		logger.info("****************************************************");
	}

	private boolean filesVerifyed() {
		logger.info("=======Begin  filesVerifyed========================");
		boolean isVerify = false;
		try {
			signGenServ.setPublicKey(
					new ByteArrayUploadedFile(signProc.getPublicKeyData(), signProc.getPublicKeyName(), TYPE));
			signGenServ.setSignVerify(new ByteArrayUploadedFile(signProc.getSignature(), signProc.getSignName(), TYPE));
			signGenServ.setSignData(new ByteArrayUploadedFile(signProc.getFileData(), signProc.getFileName(), TYPE));
			isVerify = signGenServ.verifyData();
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException
				| NullPointerException e) {
			logger.error("VERIFY ERROR check files data");
		}
		logger.info("sign verify result: " + isVerify);
		logger.info("=======Finish filesVerifyed========================");
		return isVerify;
	}

	private boolean filesForVeryfyTestIsPresent(String signId) {
		logger.info("=======Begin  filesForVeryfyTestIsPresent==========");
		Optional<SignProcess> signProcList = service.getSignById(Long.valueOf(signId));
		signProc = signProcList.get();
		logger.info("Sign process FielName: {}", signProc.getFileName());
		logger.info("Sign process SignName: {}", signProc.getSignName());
		logger.info("Sign process PublicKeyName: {}", signProc.getPublicKeyName());
		logger.info("=======Finish filesForVeryfyTestIsPresent==========");
		return !Strings.isNullOrEmpty(signProc.getFileData().toString());
	}

	@SuppressWarnings("resource")
	private boolean notEmptyGeneratedKeyPairZipContent() {
		logger.info("=======Begin  generatedKeyPair=====================");
		signGenServ.generateKeyPair();
		int contentSize = 0;
		try {
			contentSize = signGenServ.getKeyPairZip().getStream().readAllBytes().length;
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Generated file Name {}", signGenServ.getKeyPairZip().getName());
		logger.info("=======Finish generatedKeyPair=====================");
		return contentSize != 0;
	}

}
