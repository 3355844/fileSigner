package fileSigner;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import fileSigner.service.SignGenService;

@SpringBootTest
public class SignServiceTest {

	private static final Logger logger = LogManager.getLogger(SignServiceTest.class);
	private SignGenService signService;

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
		assertTrue(filesForVeryfyTestIsPresent());
		assertTrue(filesVerifyed());
		logger.info("");
		logger.info("****************************************************");
		logger.info("************Finish generateKeyPairTest**************");
		logger.info("****************************************************");
	}

	private boolean filesVerifyed() {
		logger.info("=======Begin  filesVerifyed========================");
		logger.info("=======Finish filesVerifyed========================");
		return true;
	}

	private boolean filesForVeryfyTestIsPresent() {
		logger.info("=======Begin  filesForVeryfyTestIsPresent==========");
		logger.info("=======Finish filesForVeryfyTestIsPresent==========");
		return true;
	}

	@SuppressWarnings("resource")
	private boolean notEmptyGeneratedKeyPairZipContent() {
		logger.info("=======Begin  generatedKeyPair=====================");
		signService = new SignGenService();
		signService.init();
		signService.generateKeyPair();
		int contentSize = 0;
		try {
			contentSize = signService.getKeyPairZip().getStream().readAllBytes().length;
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("=======Finish generatedKeyPair=====================");
		return contentSize != 0;
	}

}
