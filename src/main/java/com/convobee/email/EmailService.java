package com.convobee.email;

import javax.mail.MessagingException;

public interface EmailService {
	void sendMail(final AbstractEmailContext email) throws MessagingException;
}
