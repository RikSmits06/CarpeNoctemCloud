package org.carpenoctemcloud.account;

/**
 * Entity representing the account table.
 *
 * @param id             The id of the account.
 * @param name           The name of the user.
 * @param email          The email of the user.
 * @param emailConfirmed If the email is confirmed.
 * @param isAdmin        True if the account is an admin, and can log into the admin dashboard.
 */
public record Account(int id, String name, String email, boolean emailConfirmed, boolean isAdmin) {
}
