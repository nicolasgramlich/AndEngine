package org.andengine.extension.scripting.generator.util.adt.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.SortedMap;
import java.util.TreeMap;

import org.andengine.extension.scripting.generator.util.adt.FormatterException;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:02:38 - 21.03.2012
 */
public class GenFileWriter<E extends Enum<?>> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final SortedMap<E, GenFileWriterSegment> mGenFileWriterSegments = new TreeMap<E, GenFileWriterSegment>();
	private final File mFile;
	private final IFormatter mFormatter;

	// ===========================================================
	// Constructors
	// ===========================================================

	public GenFileWriter(final File pFile, final IFormatter pFormatter) {
		final File directory = pFile.getParentFile();
		if(!directory.exists()) {
			directory.mkdirs();
		}
		this.mFile = pFile;
		this.mFormatter = pFormatter;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public GenFileWriterSegment getGenFileWriterSegment(final E pSegment) {
		if(this.mGenFileWriterSegments.containsKey(pSegment)) {
			return this.mGenFileWriterSegments.get(pSegment);
		} else {
			final GenFileWriterSegment genFileWriterSegment = new GenFileWriterSegment();
			this.mGenFileWriterSegments.put(pSegment, genFileWriterSegment);
			return genFileWriterSegment;
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void begin() {
		this.mGenFileWriterSegments.clear();
	}

	public GenFileWriterSegment append(final E pSegment, final String pString) {
		return this.getGenFileWriterSegment(pSegment).append(pString);
	}

	public GenFileWriterSegment append(final E pSegment, final String pString, final Object ... pArguments) {
		return this.append(pSegment, String.format(pString, pArguments));
	}

	public GenFileWriterSegment endLine(final E pSegment) {
		return this.getGenFileWriterSegment(pSegment).end();
	}

	public void end() throws IOException {
		final Writer writer = new FileWriter(this.mFile);

		final StringBuilder stringBuilder = new StringBuilder();
		for(final GenFileWriterSegment genFileWriterSegment : this.mGenFileWriterSegments.values()) {
			if(genFileWriterSegment.hasContent()) {
				genFileWriterSegment.end();
				stringBuilder.append(genFileWriterSegment.getContent());
			}
		}

		final String source = stringBuilder.toString();
		if(this.mFormatter == null) {
			writer.write(source);
		} else {
			try {
				writer.write(this.mFormatter.format(source));
			} catch (FormatterException e) {
				e.printStackTrace();
				writer.write(source);
			}
		}

		writer.flush();

		try {
			writer.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public GenFileWriterSegment incrementIndent(final E pSegment) {
		return this.getGenFileWriterSegment(pSegment).incrementIndent();
	}

	public GenFileWriterSegment decrementIndent(final E pSegment) {
		return this.getGenFileWriterSegment(pSegment).decrementIndent();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class GenFileWriterSegment {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private int mIndent;
		private StringBuilder mSegmentStringBuilder = new StringBuilder();
		private StringBuilder mLineStringBuilder = new StringBuilder();

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public String getContent() {
			return this.mSegmentStringBuilder.toString();
		}

		public boolean hasContent() {
			return this.mSegmentStringBuilder.length() > 0;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public GenFileWriterSegment incrementIndent() {
			this.mIndent++;
			return this;
		}

		public GenFileWriterSegment decrementIndent() {
			if(this.mIndent == 0) {
				throw new IllegalStateException();
			}
			this.mIndent--;
			return this;
		}

		public GenFileWriterSegment space() {
			this.mLineStringBuilder.append(' ');
			return this;
		}

		public GenFileWriterSegment newline() {
			this.mLineStringBuilder.append('\n');
			return this;
		}

		public GenFileWriterSegment append(final String pString) {
			this.mLineStringBuilder.append(pString);
			return this;
		}

		public GenFileWriterSegment end() {
			if(this.mLineStringBuilder.length() > 0) {
				for(int i = 0; i < this.mIndent; i++) {
					this.mSegmentStringBuilder.append('\t');
				}
				this.mSegmentStringBuilder.append(this.mLineStringBuilder);
				this.mLineStringBuilder.setLength(0);
			}
			this.mSegmentStringBuilder.append('\n');
			return this;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
