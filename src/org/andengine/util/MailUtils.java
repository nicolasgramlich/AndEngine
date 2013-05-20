package org.andengine.util;

import org.andengine.util.mime.MIMEType;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 14:34:09 - 14.04.2013
 */
public final class MailUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private MailUtils() {

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

	public static void sendMail(final Context pContext, final int pIntentChooserTitleResID, final String [] pRecipients, final int pSubjectResID, final int pBodyResID) {
		final String intentChooserTitle = pContext.getString(pIntentChooserTitleResID);
		final String subject = pContext.getString(pSubjectResID);
		final String body = pContext.getString(pBodyResID);
		MailUtils.sendMail(pContext, intentChooserTitle, pRecipients, subject, body);
	}

	public static void sendMail(final Context pContext, final String pIntentChooserTitle, final String [] pRecipients, final String pSubject, final String pBody) {
		final Intent sendMailIntent = MailUtils.getSendMailIntent(pContext, pRecipients, pSubject, pBody);

		pContext.startActivity(Intent.createChooser(sendMailIntent, pIntentChooserTitle));
	}

	public static Intent getSendMailIntent(final Context pContext, final String [] pRecipients, final String pSubject, final String pBody) {
		/* Attempt using ACTION_SENDTO: */
		final Intent sendToIntent = new Intent(Intent.ACTION_SENDTO);

		final StringBuilder sendToUriStringBuilder = new StringBuilder();
		sendToUriStringBuilder.append("mailto:");
		if (!org.andengine.util.TextUtils.isEmpty(pRecipients)) {
			final String recipientsString = TextUtils.join(",", pRecipients);
			sendToUriStringBuilder.append(Uri.encode(recipientsString));
		}
		if (!TextUtils.isEmpty(pSubject)) {
			sendToUriStringBuilder.append("?subject=").append(Uri.encode(pSubject));
		}
		if (!TextUtils.isEmpty(pBody)) {
			if (TextUtils.isEmpty(pSubject)) {
				sendToUriStringBuilder.append("?body=");
			} else {
				sendToUriStringBuilder.append("&body=");
			}
			sendToUriStringBuilder.append(Uri.encode(pBody));
		}

		final String sendToUriString = sendToUriStringBuilder.toString();
		final Uri sendToUri = Uri.parse(sendToUriString);
		sendToIntent.setData(sendToUri);

		if (IntentUtils.isIntentResolvable(pContext, sendToIntent)) {
			return sendToIntent;
		} else {
			/* Fallback using ACTION_SEND: */
			final Intent sendIntent = new Intent(Intent.ACTION_SEND);

			sendIntent.setType(MIMEType.TEXT.getTypeString());
			if (!org.andengine.util.TextUtils.isEmpty(pRecipients)) {
				sendIntent.putExtra(Intent.EXTRA_EMAIL, pRecipients);
			}
			if (!TextUtils.isEmpty(pSubject)) {
				sendIntent.putExtra(Intent.EXTRA_SUBJECT, pSubject);
			}
			if (!TextUtils.isEmpty(pBody)) {
				sendIntent.putExtra(Intent.EXTRA_TEXT, pBody);
			}

			return sendIntent;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
