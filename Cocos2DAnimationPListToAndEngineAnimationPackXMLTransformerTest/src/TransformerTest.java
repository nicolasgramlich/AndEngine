import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:23:32 - 03.05.2012
 */
public class TransformerTest {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	@Before
	public void setUp() throws Exception {
	}
	
	@After
	public void tearDown() throws Exception {
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Test
	public void test() throws Exception {
		final InputStream in = TransformerTest.class.getClassLoader().getResourceAsStream("test.plist");
		
		final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
		"<animationpack version=\"1\">\n" +
		"    <texturepacks>\n" +
		"        <texturepack filename=\"farm_storypanel_spritesheet.xml\"/>\n" +
		"    </texturepacks>\n" +
		"    <animations>\n" +
		"        <animation name=\"farm_duck_celebrate\" loopcount=\"11\">\n" +
		"            <animationframe duration=\"100\" textureregion=\"farm_duck_celebrate_0001.png\"/>\n" +
		"            <animationframe duration=\"200\" textureregion=\"farm_duck_celebrate_0002.png\"/>\n" +
		"        </animation>\n" +
		"        <animation name=\"farm_duck_fever\" loopcount=\"1\">\n" +
		"            <animationframe duration=\"600\" textureregion=\"farm_duck_fever_0001.png\"/>\n" +
		"            <animationframe duration=\"800\" textureregion=\"farm_duck_fever_0002.png\"/>\n" +
		"            <animationframe duration=\"1000\" textureregion=\"farm_duck_fever_0003.png\"/>\n" +
		"        </animation>\n" +
		"    </animations>\n" +
		"</animationpack>\n";

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Transformer.transform(in, outputStream);

		final String actual = new String(outputStream.toByteArray(), "UTF-8");

		Assert.assertEquals(expected, actual);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
