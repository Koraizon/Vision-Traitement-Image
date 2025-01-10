import ImageProcessing.Seuillage.Seuillage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.util.stream.Stream;

public class JUnit5Tests {
	File fichier;

	@BeforeAll
	static void initAll() {
	}

	@BeforeEach
	void init() {
		fichier = new File("./src/test/resources/lenaBruit.png");
		Assertions.assertNotNull(fichier);
	}

	@Test
	void succeedingTest() {
	}

	@Test()
	@DisplayName("test le seuillage simple")
	void testSeuillageSimple() throws Exception {


		int[][] imageMat =  {
				{100, 246, 6},
				{101, 200, 75},
				{99, 173, 2}
		};

		for (int[] ligne : imageMat) {
			for (int pixel : ligne) {
				if (pixel < 0 || pixel > 255)
					Assertions.fail("pixel hors norme");
			}
		}

		int[][] imageAfter = Seuillage.seuillageSimple(imageMat, 100);

		if (imageMat.length != imageAfter.length || imageMat[0].length != imageAfter[0].length) Assertions.fail();

		for (int i = 0; i < imageAfter.length; i++) {
			for (int j = 0; j < imageAfter[0].length; j++) {
				if (imageAfter[i][j] != 0 && imageAfter[i][j] != 255)
					Assertions.fail();
			}
		}
	}

	@ParameterizedTest
	@MethodSource("ImageMatrices")
	public void testSeuillage(int[][] matrix) {
		int[][] result = Seuillage.seuillageSimple(matrix, 100);

		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				if (result[i][j] != 0 && result[i][j] != 255)
					Assertions.fail();
			}
		}
	}

	private static Stream<Object[]> ImageMatrices() {
		return Stream.of(
				new Object[]{new int[][]{{100, 200, 3}, {24, 177, 4}, {3, 4, 1}}},
				new Object[]{new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}}
		);
	}

	@Test
	void failingTest() {
		fail("a failing test");
	}

	@Test
	@Disabled("for demonstration purposes")
	void skippedTest() {
		// not executed
	}

	@Test
	void abortedTest() {
		assumeTrue("abc".contains("Z"));
		fail("test should have been aborted");
	}

	@AfterEach
	void tearDown() {
		System.out.println("test fini");
	}

	@AfterAll
	static void tearDownAll() {
		System.out.println("tests fini");
	}



}
