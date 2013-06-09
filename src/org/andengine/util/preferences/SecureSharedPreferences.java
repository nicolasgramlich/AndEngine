package org.andengine.util.preferences;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.andengine.util.exception.MethodNotYetImplementedException;
import org.andengine.util.preferences.exception.SecureSharedPreferencesException;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Base64;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 20:09:38 - 13.04.2013
 */
public class SecureSharedPreferences implements SharedPreferences {
	// ===========================================================
	// Constants
	// ===========================================================

	protected static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
	protected static final String KEY_HASH_TRANSFORMATION = "SHA-256";
	protected static final String CHARSET = "UTF-8";

	// ===========================================================
	// Fields
	// ===========================================================

	protected final SharedPreferences mDelegate;

	protected final boolean mEncryptKeys;
	protected final boolean mEncryptValues;

	protected final Cipher mEncryptCipher;
	protected final Cipher mDecryptCipher;

	protected final ReadWriteLock mReadWriteLock = new ReentrantReadWriteLock(true);

	// ===========================================================
	// Constructors
	// ===========================================================

	public SecureSharedPreferences(final SharedPreferences pDelegate, final String pSecureKey) throws SecureSharedPreferencesException {
		this(pDelegate, pSecureKey, true, true);
	}

	public SecureSharedPreferences(final SharedPreferences pDelegate, final String pSecureKey, final boolean pEncryptKeys, final boolean pEncryptValues) throws SecureSharedPreferencesException {
		this.mDelegate = pDelegate;
		this.mEncryptKeys = pEncryptKeys;
		this.mEncryptValues = pEncryptValues;
		try {
			this.mEncryptCipher = Cipher.getInstance(SecureSharedPreferences.CIPHER_TRANSFORMATION);
			this.mDecryptCipher = Cipher.getInstance(SecureSharedPreferences.CIPHER_TRANSFORMATION);

			final IvParameterSpec ivSpec = this.getIvParameterSpec();
			final SecretKeySpec secretKey = this.getSecretKeySpec(pSecureKey);

			this.mEncryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
			this.mDecryptCipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
		} catch (final GeneralSecurityException e) {
			throw new SecureSharedPreferencesException(e);
		} catch (final UnsupportedEncodingException e) {
			throw new SecureSharedPreferencesException(e);
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Lock getReadLock() {
		return this.mReadWriteLock.readLock();
	}

	public Lock getWriteLock() {
		return this.mReadWriteLock.writeLock();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Editor edit() {
		return new Editor();
	}

	@Override
	public Map<String, ?> getAll() {
		throw new MethodNotYetImplementedException();
	}

	@Override
	public boolean getBoolean(final String pKey, final boolean pDefaultValue) {
		final String encryptedKey = this.encryptKey(pKey);
		final String value = this.mDelegate.getString(encryptedKey, null);
		if (value == null) {
			return pDefaultValue;
		} else {
			final String decryptedValue = this.decryptValue(value);
			return Boolean.parseBoolean(decryptedValue);
		}
	}

	@Override
	public int getInt(final String pKey, final int pDefaultValue) {
		final String encryptedKey = this.encryptKey(pKey);
		final String value = this.mDelegate.getString(encryptedKey, null);
		if (value == null) {
			return pDefaultValue;
		} else {
			final String decryptedValue = this.decryptValue(value);
			return Integer.parseInt(decryptedValue);
		}
	}

	@Override
	public long getLong(final String pKey, final long pDefaultValue) {
		final String encryptedKey = this.encryptKey(pKey);
		final String value = this.mDelegate.getString(encryptedKey, null);
		if (value == null) {
			return pDefaultValue;
		} else {
			final String decryptedValue = this.decryptValue(value);
			return Long.parseLong(decryptedValue);
		}
	}

	@Override
	public float getFloat(final String pKey, final float pDefaultValue) {
		final String encryptedKey = this.encryptKey(pKey);
		final String value = this.mDelegate.getString(encryptedKey, null);
		if (value == null) {
			return pDefaultValue;
		} else {
			final String decryptedValue = this.decryptValue(value);
			return Float.parseFloat(decryptedValue);
		}
	}

	@Override
	public String getString(final String pKey, final String pDefaultValue) {
		final String encryptedKey = this.encryptKey(pKey);
		final String value = this.mDelegate.getString(encryptedKey, null);
		if (value == null) {
			return pDefaultValue;
		} else {
			final String decryptedValue = this.decryptValue(value);
			return decryptedValue;
		}
	}

	@Override
	public boolean contains(final String pKey) {
		return this.mDelegate.contains(pKey);
	}

	@Override
	public void registerOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener pOnSharedPreferenceChangeListener) {
		this.mDelegate.registerOnSharedPreferenceChangeListener(pOnSharedPreferenceChangeListener);
	}

	@Override
	public void unregisterOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener pOnSharedPreferenceChangeListener) {
		this.mDelegate.unregisterOnSharedPreferenceChangeListener(pOnSharedPreferenceChangeListener);
	}

	@Override
	public Set<String> getStringSet(final String pKey, final Set<String> pStringSet) {
		throw new MethodNotYetImplementedException();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected String encryptKey(final String pKey) {
		if (this.mEncryptKeys) {
			return this.encrypt(pKey);
		} else {
			return pKey;
		}
	}

	protected String encryptValue(final String pValue) {
		if (this.mEncryptValues) {
			return this.encrypt(pValue);
		} else {
			return pValue;
		}
	}

	protected String decryptKey(final String pKey) {
		if (this.mEncryptKeys) {
			return this.decrypt(pKey);
		} else {
			return pKey;
		}
	}

	protected String decryptValue(final String pValue) {
		if (this.mEncryptValues) {
			return this.decrypt(pValue);
		} else {
			return pValue;
		}
	}

	protected IvParameterSpec getIvParameterSpec() {
		final int blockSize = this.mEncryptCipher.getBlockSize();
		final byte[] iv = new byte[blockSize];
		System.arraycopy("abcdefghijklmnopqrstuvwxzyABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".getBytes(), 0, iv, 0, blockSize);
		return new IvParameterSpec(iv);
	}

	protected SecretKeySpec getSecretKeySpec(final String pKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		final byte[] keyBytes = this.createKeyBytes(pKey);
		return new SecretKeySpec(keyBytes, SecureSharedPreferences.CIPHER_TRANSFORMATION);
	}

	protected byte[] createKeyBytes(final String pKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		final MessageDigest md = MessageDigest.getInstance(SecureSharedPreferences.KEY_HASH_TRANSFORMATION);
		md.reset();
		final byte[] keyBytes = md.digest(pKey.getBytes(SecureSharedPreferences.CHARSET));
		return keyBytes;
	}

	protected String encrypt(final String pPlainText) throws SecureSharedPreferencesException {
		byte[] secureValue;
		try {
			secureValue = SecureSharedPreferences.crypt(this.mEncryptCipher, pPlainText.getBytes(SecureSharedPreferences.CHARSET));
		} catch (final UnsupportedEncodingException e) {
			throw new SecureSharedPreferencesException(e);
		}
		final String secureValueEncoded = Base64.encodeToString(secureValue, Base64.NO_WRAP);
		return secureValueEncoded;
	}

	protected String decrypt(final String pCipherText) {
		final byte[] securedValue = Base64.decode(pCipherText, Base64.NO_WRAP);
		final byte[] pValue = SecureSharedPreferences.crypt(this.mDecryptCipher, securedValue);
		try {
			return new String(pValue, SecureSharedPreferences.CHARSET);
		} catch (final UnsupportedEncodingException e) {
			throw new SecureSharedPreferencesException(e);
		}
	}

	protected static byte[] crypt(final Cipher pCipher, final byte[] pBytes) throws SecureSharedPreferencesException {
		try {
			return pCipher.doFinal(pBytes);
		} catch (final Exception e) {
			throw new SecureSharedPreferencesException(e);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class Editor implements SharedPreferences.Editor {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		protected final SharedPreferences.Editor mDelegate;

		// ===========================================================
		// Constructors
		// ===========================================================

		public Editor() {
			this.mDelegate = SecureSharedPreferences.this.mDelegate.edit();
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public Editor putBoolean(final String pKey, final boolean pValue) {
			final String encryptedKey = SecureSharedPreferences.this.encryptKey(pKey);
			final String encryptedValue = SecureSharedPreferences.this.encryptValue(Boolean.toString(pValue));
			this.mDelegate.putString(encryptedKey, encryptedValue);
			return this;
		}

		@Override
		public Editor putInt(final String pKey, final int pValue) {
			final String encryptedKey = SecureSharedPreferences.this.encrypt(pKey);
			final String encryptValue = SecureSharedPreferences.this.encryptValue(Integer.toString(pValue));
			this.mDelegate.putString(encryptedKey, encryptValue);
			return this;
		}

		@Override
		public Editor putLong(final String pKey, final long pValue) {
			final String encryptedKey = SecureSharedPreferences.this.encrypt(pKey);
			final String encryptValue = SecureSharedPreferences.this.encryptValue(Long.toString(pValue));
			this.mDelegate.putString(encryptedKey, encryptValue);
			return this;
		}

		@Override
		public Editor putFloat(final String pKey, final float pValue) {
			final String encryptedKey = SecureSharedPreferences.this.encrypt(pKey);
			final String encryptedVaue = SecureSharedPreferences.this.encryptValue(Float.toString(pValue));
			this.mDelegate.putString(encryptedKey, encryptedVaue);
			return this;
		}

		@Override
		public Editor putString(final String pKey, final String pValue) {
			final String encryptedKey = SecureSharedPreferences.this.encrypt(pKey);
			final String encryptValue = SecureSharedPreferences.this.encryptValue(pValue);
			this.mDelegate.putString(encryptedKey, encryptValue);
			return this;
		}

		@Override
		public Editor putStringSet(final String pKey, final Set<String> pStringSet) {
			throw new MethodNotYetImplementedException();
		}

		@Override
		public Editor remove(final String pKey) {
			final String encryptedKey = SecureSharedPreferences.this.encrypt(pKey);
			this.mDelegate.remove(encryptedKey);
			return this;
		}

		@Override
		public Editor clear() {
			this.mDelegate.clear();
			return this;
		}

		@Override
		public boolean commit() {
			return this.mDelegate.commit();
		}

		@TargetApi(Build.VERSION_CODES.GINGERBREAD)
		@Override
		public void apply() {
			this.mDelegate.apply();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}