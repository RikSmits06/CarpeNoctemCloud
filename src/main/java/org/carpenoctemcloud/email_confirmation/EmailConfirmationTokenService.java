package org.carpenoctemcloud.email_confirmation;

import org.carpenoctemcloud.auth.AuthConfiguration;
import org.carpenoctemcloud.auth.AuthUtil;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class EmailConfirmationTokenService {

    private final NamedParameterJdbcTemplate template;

    public EmailConfirmationTokenService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /**
     * Generates and stores a new token to confirm an email.
     * Will fail if the email is already confirmed.
     *
     * @param email The email to confirm.
     * @return The token which was generated.
     */
    public String generateConfirmationTokenByEmail(String email) {
        String token = AuthUtil.randomToken(12);
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("token", token).addValue("email", email)
                        .addValue("expireHours",
                                  AuthConfiguration.MAX_AGE_EMAIL_CONFIRMATION_IN_HOURS);
        template.update("""
                                with found_account as
                                    (select * from account
                                              where email=:email
                                                and not email_confirmed
                                              limit 1)
                                insert
                                into email_confirmation_token(account_id, token, expiry)
                                       select fa.id, :token, now() + interval '1' hour * :expireHours
                                from found_account fa;
                                """, source);
        return token;
    }

    /**
     * Cleans up all the tokens that have expired.
     */
    public void cleanupOldTokens() {
        template.update("""
                                delete
                                from email_confirmation_token
                                where expiry <= now();
                                """, new MapSqlParameterSource());
    }
}
