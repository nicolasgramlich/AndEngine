import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 10:29:12 - 03.05.2012
 */
public class Transformer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	@Option(required = true, name = "-in")
	private File mInputFile;

	@Option(required = true, name = "-out")
	private File mOutputFile;

	// ===========================================================
	// Constructors
	// ===========================================================

	public static void main(final String[] pArgs) throws Exception {
		new Transformer(pArgs);
	}

	public Transformer(final String[] pArgs) throws CmdLineException, TransformerException, FileNotFoundException{
		final CmdLineParser parser = new CmdLineParser(this);
		parser.parseArgument(pArgs);

		Transformer.transform(new FileInputStream(this.mInputFile), new FileOutputStream(this.mOutputFile));
	}

	public static void transform(final InputStream pInputStream, final OutputStream pOutputStream) throws TransformerException {
		Transformer.transform(pInputStream, "transform.xslt", pOutputStream);
	}

	private static void transform(final InputStream pInputStream, final String pXSLTResourceName, final OutputStream pOutputStream) throws TransformerException {
		final InputStream xsltInputStream = Transformer.class.getClassLoader().getResourceAsStream(pXSLTResourceName);

		Transformer.transform(pInputStream, xsltInputStream, pOutputStream);
	}

	private static void transform(final InputStream pInputStream, final InputStream pXSLTInputStream, final OutputStream pOutputStream) throws TransformerException {
		final Source xmlSource = new StreamSource(pInputStream);
		final Source xsltSource = new StreamSource(pXSLTInputStream);
		final Result result = new StreamResult(pOutputStream);

		/* Create an instance of TransformerFactory. */
		final TransformerFactory transFact = TransformerFactory.newInstance();

		final javax.xml.transform.Transformer transformer = transFact.newTransformer(xsltSource);
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(xmlSource, result);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
