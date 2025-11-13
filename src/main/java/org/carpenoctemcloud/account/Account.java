package org.carpenoctemcloud.account;

public record Account(int id, String name, String email, boolean emailConfirmed, boolean isAdmin,
                      String password, String salt) {
}
