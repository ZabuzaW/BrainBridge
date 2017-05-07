package de.zabuza.brainbridge.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Utility class that provides methods for the hyper text transfer protocol
 * (HTTP).
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class HttpUtil {

	/**
	 * Constant for an empty answer text.
	 */
	private static final String EMPTY_ANSWER = "";

	/**
	 * The charset to use for encoding and decoding text.
	 */
	private static final Charset TEXT_CHARSET = StandardCharsets.UTF_8;

	/**
	 * Sends an error answer with the given status to the given client by using
	 * the HTTP/1.0 protocol.
	 * 
	 * @param status
	 *            The status of the error answer to send
	 * @param client
	 *            Client to send to
	 * @throws IOException
	 *             If an I/O-Exception occurred
	 */
	public static void sendError(final EHttpStatus status, final Socket client) throws IOException {
		sendHttpAnswer(EHttpContentType.TEXT, status, client);
	}

	/**
	 * Sends an empty answer with the given parameters to the given client by
	 * using the HTTP/1.0 protocol.
	 * 
	 * @param client
	 *            Client to send to
	 * @param contentType
	 *            Type of the content to send
	 * @param status
	 *            The status of the answer to send
	 * @throws IOException
	 *             If an I/O-Exception occurred.
	 */
	public static void sendHttpAnswer(final EHttpContentType contentType, final EHttpStatus status, final Socket client)
			throws IOException {
		sendHttpAnswer(EMPTY_ANSWER, contentType, status, client);
	}

	/**
	 * Sends the given answer with the given parameters to the given client by
	 * using the HTTP/1.0 protocol.
	 * 
	 * @param answerText
	 *            Answer to send
	 * @param client
	 *            Client to send to
	 * @param contentType
	 *            Type of the content to send
	 * @param status
	 *            The status of the answer to send
	 * @throws IOException
	 *             If an I/O-Exception occurred.
	 */
	public static void sendHttpAnswer(final String answerText, final EHttpContentType contentType,
			final EHttpStatus status, final Socket client) throws IOException {
		EHttpStatus statusToUse = status;
		String answerTextToUse = answerText;

		final String contentTypeText;
		if (contentType == EHttpContentType.TEXT) {
			contentTypeText = "text/plain";
		} else if (contentType == EHttpContentType.HTML) {
			contentTypeText = "text/html";
		} else if (contentType == EHttpContentType.CSS) {
			contentTypeText = "text/css";
		} else if (contentType == EHttpContentType.JS) {
			contentTypeText = "application/javascript";
		} else if (contentType == EHttpContentType.JSON) {
			contentTypeText = "application/json";
		} else if (contentType == EHttpContentType.PNG) {
			contentTypeText = "image/png";
		} else if (contentType == EHttpContentType.JPG) {
			contentTypeText = "image/jpeg";
		} else {
			contentTypeText = "text/plain";
			statusToUse = EHttpStatus.INTERNAL_SERVER_ERROR;
			// In case of an server error inside this method, don't send the
			// intended message. It might contain sensible data.
			answerTextToUse = "";
		}

		final int statusNumber;
		if (statusToUse == EHttpStatus.OK) {
			statusNumber = 200;
		} else if (statusToUse == EHttpStatus.NO_CONTENT) {
			statusNumber = 204;
		} else if (statusToUse == EHttpStatus.BAD_REQUEST) {
			statusNumber = 400;
		} else if (statusToUse == EHttpStatus.FORBIDDEN) {
			statusNumber = 403;
		} else if (statusToUse == EHttpStatus.NOT_FOUND) {
			statusNumber = 404;
		} else if (statusToUse == EHttpStatus.INTERNAL_SERVER_ERROR) {
			statusNumber = 500;
		} else if (statusToUse == EHttpStatus.NOT_IMPLEMENTED) {
			statusNumber = 501;
		} else {
			statusToUse = EHttpStatus.INTERNAL_SERVER_ERROR;
			statusNumber = 500;
			// In case of an server error inside this method, don't send the
			// intended message. It might contain sensible data.
			answerTextToUse = "";
		}

		final byte[] answerTextAsBytes = answerTextToUse.getBytes(TEXT_CHARSET);
		final String charset = TEXT_CHARSET.displayName().toLowerCase();

		final String nextLine = "\r\n";
		final StringBuilder answer = new StringBuilder();
		answer.append("HTTP/1.0 " + statusNumber + " " + statusToUse + nextLine);
		answer.append("Content-Length: " + answerTextAsBytes.length + nextLine);
		answer.append("Content-Type: " + contentTypeText + ", charset=" + charset + nextLine);
		answer.append("Connection: close" + nextLine);
		answer.append(nextLine);
		answer.append(answerTextToUse);

		try (final DataOutputStream output = new DataOutputStream(client.getOutputStream())) {
			output.write(answer.toString().getBytes(TEXT_CHARSET));
		}
	}

	/**
	 * Utility class. No implementation.
	 */
	private HttpUtil() {

	}
}
