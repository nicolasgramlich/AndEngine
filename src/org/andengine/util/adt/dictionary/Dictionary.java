package org.andengine.util.adt.dictionary;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.andengine.util.FileUtils;
import org.andengine.util.StreamUtils;
import org.andengine.util.adt.bit.BitVector;
import org.andengine.util.adt.bit.ByteBackedBitVector;
import org.andengine.util.adt.bit.IBitVector;
import org.andengine.util.adt.bit.LongBackedBitVector;
import org.andengine.util.adt.data.DataUtils;


/**
 * @author Nicolas Gramlich
 * @since Nov 20, 2012
 */
public class Dictionary {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final short VERSION = 1;

	private static final Comparator<String> CASEINSENSITIVE_REVERSE_LEXICOGRAPHICAL_COMPARATOR = new Comparator<String>() {
		@Override
		public int compare(final String pStringA, final String pStringB) {
			return -(pStringA.compareTo(pStringB));
		}
	};

	private static final int OFFSET_VERSION = 0;
	private static final int SIZE_VERSION = Short.SIZE;

	private static final int OFFSET_CHARACTER_COUNT = Dictionary.OFFSET_VERSION + Dictionary.SIZE_VERSION;
	private static final int SIZE_CHARACTER_COUNT = Integer.SIZE;

	private static final int OFFSET_CHARACTER_BITLENGTH = Dictionary.OFFSET_CHARACTER_COUNT + Dictionary.SIZE_CHARACTER_COUNT;
	private static final int SIZE_CHARACTER_BITLENGTH = Byte.SIZE;

	private static final int OFFSET_INDEXENTRY_COUNT = Dictionary.OFFSET_CHARACTER_BITLENGTH + Dictionary.SIZE_CHARACTER_BITLENGTH;
	private static final int SIZE_INDEXENTRY_COUNT = Integer.SIZE;

	private static final int OFFSET_INDEXENTRY_STARTINDEX_BITLENGTH = Dictionary.OFFSET_INDEXENTRY_COUNT + Dictionary.SIZE_INDEXENTRY_COUNT;
	private static final int SIZE_INDEXENTRY_STARTINDEX_BITLENGTH = Byte.SIZE;

	private static final int OFFSET_INDEXENTRY_LENGTH_BITLENGTH = Dictionary.OFFSET_INDEXENTRY_STARTINDEX_BITLENGTH + Dictionary.SIZE_INDEXENTRY_STARTINDEX_BITLENGTH;
	private static final int SIZE_INDEXENTRY_LENGTH_BITLENGTH = Byte.SIZE;

	private static final int OFFSET_CHARACTERS = Dictionary.OFFSET_INDEXENTRY_LENGTH_BITLENGTH + Dictionary.SIZE_INDEXENTRY_LENGTH_BITLENGTH;

	// ===========================================================
	// Fields
	// ===========================================================

	private final short mVersion;
	private final IBitVector mBitVector;

	private final int mCharacterCount;
	private final int mCharacterBitLength;
	private final char[] mCharacterTable;

	private final int mEntryCount;
	private final int mEntryStartIndexBitLength;
	private final int mEntryLengthBitLength;

	private final int mEntriesOffset;
	private final int mCharacterSequenceOffset;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Dictionary(final byte[] pBytes) {
		this(new LongBackedBitVector(pBytes));
	}

	public Dictionary(final IBitVector pBitVector) {
		this.mBitVector = pBitVector;

		this.mVersion = this.mBitVector.getShort(Dictionary.OFFSET_VERSION);
		if (this.mVersion != Dictionary.VERSION) {
			throw new IllegalArgumentException("Illegal version: " + this.mVersion);
		}

		this.mCharacterCount = this.mBitVector.getInt(Dictionary.OFFSET_CHARACTER_COUNT);
		this.mCharacterBitLength = this.mBitVector.getByte(Dictionary.OFFSET_CHARACTER_BITLENGTH);
		this.mCharacterTable = new char[this.mCharacterCount];
		for (int i = 0; i < this.mCharacterCount; i++) {
			this.mCharacterTable[i] = (char) this.mBitVector.getShort(Dictionary.OFFSET_CHARACTERS + (i * Character.SIZE));
		}

		this.mEntryCount = this.mBitVector.getInt(Dictionary.OFFSET_INDEXENTRY_COUNT);
		this.mEntryStartIndexBitLength = this.mBitVector.getByte(Dictionary.OFFSET_INDEXENTRY_STARTINDEX_BITLENGTH);
		this.mEntryLengthBitLength = this.mBitVector.getByte(Dictionary.OFFSET_INDEXENTRY_LENGTH_BITLENGTH);

		this.mEntriesOffset = Dictionary.OFFSET_CHARACTERS + (this.mCharacterCount * Character.SIZE);
		this.mCharacterSequenceOffset = this.mEntriesOffset + (this.mEntryCount * (this.mEntryStartIndexBitLength + this.mEntryLengthBitLength));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getVersion() {
		return this.mVersion;
	}

	public int getEntryCount() {
		return this.mEntryCount;
	}

	public String getEntry(final int pIndex) {
		if ((pIndex < 0) || (pIndex > this.mEntryCount)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		final int offsetInEntries = pIndex * (this.mEntryStartIndexBitLength + this.mEntryLengthBitLength);
		final int entryOffset = this.mEntriesOffset + offsetInEntries;

		final int startIndex = this.mBitVector.getBits(entryOffset, this.mEntryStartIndexBitLength);
		final int length = this.mBitVector.getBits(entryOffset + this.mEntryStartIndexBitLength, this.mEntryLengthBitLength);

		final char[] chars = new char[length]; // TODO Try to avoid allocation

		for (int i = 0; i < length; i++) {
			final int characterOffset = this.mCharacterSequenceOffset + ((i + startIndex) * this.mCharacterBitLength);

			final int characterBits = this.mBitVector.getBits(characterOffset, this.mCharacterBitLength);
			final char character = this.mCharacterTable[characterBits];

			chars[i] = character;
		}

		return new String(chars);
	}

	public String[] getEntries() {
		final String[] entries = new String[this.mEntryCount];

		for (int i = 0; i < this.mEntryCount; i++) {
			entries[i] = this.getEntry(i);
		}

		return entries;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void save(final OutputStream pOutputStream) throws IOException {
		this.mBitVector.save(new DataOutputStream(pOutputStream));
	}

	public static Dictionary load(final InputStream pInputStream) throws IOException {
		return new Dictionary(ByteBackedBitVector.load(new DataInputStream(pInputStream)));
	}

	public boolean contains(final String pString) {
		if (pString == null) {
			throw new IllegalArgumentException("pCharSequence must not be null");
		}

		if (pString.length() < 0) {
			throw new IllegalArgumentException("pCharSequence must not be empty");
		}

		int lowerBound = 0;
		int higherBound = this.mEntryCount - 1;

		/* Binary search. */
		while (lowerBound <= higherBound) {
			final int mid = ((lowerBound + higherBound) >>> 1);

			// TODO Optimization: don't decode the whole String, but compare char by char?
			final String midEntry = this.getEntry(mid);
			final int compared = midEntry.compareTo(pString);

			if (compared > 0) {
				lowerBound = mid + 1;
			} else if (compared < 0) {
				higherBound = mid - 1;
			} else {
				return true;
			}
		}

		return false;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class Factory {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public static Dictionary create(final File pFile) throws IOException {
			return Dictionary.Factory.create(FileUtils.readLines(pFile));
		}

		public static Dictionary create(final InputStream pInputStream) throws IOException {
			return Dictionary.Factory.create(StreamUtils.readLines(pInputStream));
		}

		public static Dictionary create(final Reader pReader) throws IOException {
			return Dictionary.Factory.create(StreamUtils.readLines(pReader));
		}

		public static Dictionary create(final String ... pStrings) {
			Arrays.sort(pStrings, Dictionary.CASEINSENSITIVE_REVERSE_LEXICOGRAPHICAL_COMPARATOR);

			final Dictionary.Factory.Entries entryDictionary = Dictionary.Factory.createDictionary(pStrings);

			final String string = entryDictionary.mString;

			final int characterCountTotal = string.length();
			final int characterCount = entryDictionary.mCharacters.length;
			final int characterBitLength = DataUtils.getBitLength(entryDictionary.mCharacters.length);
			final int entryCount = entryDictionary.mEntries.size();
			final int entryStartIndexBitLength = DataUtils.getBitLength(entryDictionary.mEntryStartIndexMaximum);
			final int entryLengthBitLength = DataUtils.getBitLength(entryDictionary.mEntryLengthMaximum);

			final int bitsRequired = Dictionary.Factory.calculateBitsRequired(characterCountTotal, characterCount, characterBitLength, entryCount, entryStartIndexBitLength, entryLengthBitLength);

			final IBitVector bitVector = new ByteBackedBitVector(bitsRequired);

			/* Write meta data. */
			bitVector.setShort(Dictionary.OFFSET_VERSION, Dictionary.VERSION);
			bitVector.setInt(Dictionary.OFFSET_CHARACTER_COUNT, characterCount);
			bitVector.setByte(Dictionary.OFFSET_CHARACTER_BITLENGTH, (byte) characterBitLength);
			bitVector.setInt(Dictionary.OFFSET_INDEXENTRY_COUNT, entryCount);
			bitVector.setByte(Dictionary.OFFSET_INDEXENTRY_STARTINDEX_BITLENGTH, (byte) entryStartIndexBitLength);
			bitVector.setByte(Dictionary.OFFSET_INDEXENTRY_LENGTH_BITLENGTH, (byte) entryLengthBitLength);

			/* Character table. */
			for (int i = 0; i < characterCount; i++) {
				final char character = entryDictionary.mCharacters[i];
				final short characterBits = (short) character;
				bitVector.setShort(Dictionary.OFFSET_CHARACTERS + (i * Character.SIZE), characterBits);
			}

			/* Index entries. */
			final int offsetEntries = Dictionary.OFFSET_CHARACTERS + (characterCount * Character.SIZE);
			for (int i = 0; i < entryCount; i++) {
				final Entry entry = entryDictionary.mEntries.get(i);

				final int entryStartIndexBits = entry.mStartIndex; // TODO Fix bug when this goes over 2048? Maybe it's masking in the decoder?
				final int entryLengthBits = entry.mLength;

				final int offsetInEntries = i * (entryStartIndexBitLength + entryLengthBitLength);
				final int offsetEntryStartIndex = offsetEntries + offsetInEntries;
				final int offsetEntryLength = offsetEntryStartIndex + entryStartIndexBitLength;

				bitVector.setBits(offsetEntryStartIndex, entryStartIndexBits, Integer.SIZE - entryStartIndexBitLength, entryStartIndexBitLength);
				bitVector.setBits(offsetEntryLength, entryLengthBits, Integer.SIZE - entryLengthBitLength, entryLengthBitLength);
			}

			final int offsetCharacterString = offsetEntries + (entryCount * (entryStartIndexBitLength + entryLengthBitLength));

			/* Character string. */
			for (int i = 0; i < characterCountTotal; i++) {
				final int offsetInCharacterString = i * characterBitLength;
				final int offsetCharacter = offsetCharacterString + offsetInCharacterString;

				final char character = string.charAt(i);
				final int characterBits = Arrays.binarySearch(entryDictionary.mCharacters, character); // TODO Use short?

				bitVector.setBits(offsetCharacter, characterBits, Integer.SIZE - characterBitLength, characterBitLength);
			}

			return new Dictionary(bitVector);
		}

		private static int calculateBitsRequired(final int pCharacterCountTotal, final int pCharacterCount, final int pCharacterBitLength, final int pEntryCount, final int pEntryStartIndexBitLength, final int pEntryLengthBitLength) {
			return Dictionary.SIZE_VERSION
					+ Dictionary.SIZE_CHARACTER_COUNT
					+ Dictionary.SIZE_CHARACTER_BITLENGTH
					+ Dictionary.SIZE_INDEXENTRY_COUNT
					+ Dictionary.SIZE_INDEXENTRY_STARTINDEX_BITLENGTH
					+ Dictionary.SIZE_INDEXENTRY_LENGTH_BITLENGTH
					+ (pCharacterCount * Character.SIZE)
					+ (pEntryCount * (pEntryStartIndexBitLength + pEntryLengthBitLength))
					+ (pCharacterBitLength * pCharacterCountTotal);
		}

		private static Entries createDictionary(final String ... pStrings) {
			final ArrayList<Entry> entries = new ArrayList<Entry>();
			final StringBuilder stringBuilder = new StringBuilder();

			for (final String string : pStrings) {
				final int existingStringStartIndex = stringBuilder.indexOf(string);

				final int stringBuilderLength = stringBuilder.length();
				final int stringLength = string.length();
				if (existingStringStartIndex >= 0) {
					/* Reference string. */
					final int startIndex = existingStringStartIndex;
					entries.add(new Entry(startIndex, stringLength));
				} else {
					/* Check if the stringbuilder ends with any prefix of the string. */
					final int prefixLength = Dictionary.Factory.getPrefixLength(stringBuilder, string);
					if (prefixLength == -1) {
						/* Append new string. */
						final int startIndex = stringBuilderLength;
						entries.add(new Entry(startIndex, stringLength));
						stringBuilder.append(string);
					} else {
						/* Append remainder of string after prefix. */
						final int startIndex = stringBuilderLength - prefixLength;
						entries.add(new Entry(startIndex, stringLength));
						stringBuilder.append(string.substring(prefixLength));
					}
				}
			}

			return new Dictionary.Factory.Entries(entries, stringBuilder.toString());
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

		private static int getPrefixLength(final StringBuilder pStringBuilder, final String pString) {
			final int stringBuilderLength = pStringBuilder.length();

			for (int prefixLength = pString.length() - 1; prefixLength > 0; prefixLength--) {
				final String prefix = pString.substring(0, prefixLength);
				final int prefixLastIndexStart = pStringBuilder.indexOf(prefix, stringBuilderLength - prefixLength);
				if (prefixLastIndexStart >= 0) {
					return prefixLength;
				}
			}

			return -1;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================

		public static class Entries {
			// ===========================================================
			// Constants
			// ===========================================================

			// ===========================================================
			// Fields
			// ===========================================================

			private final ArrayList<Entry> mEntries;
			private final String mString;

			private final int mEntryStartIndexMaximum;
			private final int mEntryLengthMaximum;
			private final char[] mCharacters;

			// ===========================================================
			// Constructors
			// ===========================================================

			public Entries(final ArrayList<Entry> pEntries, final String pString) {
				this.mEntries = pEntries;
				this.mString = pString;

				int entryStartIndexMaximum = 0;
				int entryLengthMaximum = 0;
				for (final Entry entry : pEntries) {
					entryStartIndexMaximum = Math.max(entryStartIndexMaximum, entry.mStartIndex);
					entryLengthMaximum = Math.max(entryLengthMaximum, entry.mLength);
				}

				this.mEntryStartIndexMaximum = entryStartIndexMaximum;
				this.mEntryLengthMaximum = entryLengthMaximum;

				/* Determine all characters used. */
				final IBitVector characterBitVector = new ByteBackedBitVector(Character.MAX_VALUE);

				final int stringLength = pString.length();
				for (int i = 0; i < stringLength; i++) {
					characterBitVector.setBit(pString.charAt(i));
				}

				int characterCount = 0;
				for (int i = 0; i < Character.MAX_VALUE; i++) {
					if (characterBitVector.getBit(i) == BitVector.TRUE) {
						characterCount++;
					}
				}
				this.mCharacters = new char[characterCount];

				characterCount = 0;
				for (int i = 0; i < Character.MAX_VALUE; i++) {
					if (characterBitVector.getBit(i) == BitVector.TRUE) {
						this.mCharacters[characterCount++] = (char) i;
					}
				}
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

		public static class Entry {
			// ===========================================================
			// Constants
			// ===========================================================

			// ===========================================================
			// Fields
			// ===========================================================

			private final int mStartIndex;
			private final int mLength;

			// ===========================================================
			// Constructors
			// ===========================================================

			public Entry(final int pStartIndex, final int pLength) {
				this.mStartIndex = pStartIndex;
				this.mLength = pLength;
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
	}
}
