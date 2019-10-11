/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.udg.caes.signin.userAccount;

public final class UserAccount {
    private final String id;
    private String password;
    private boolean signedIn;
    private boolean revoked;

    public UserAccount(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public boolean isSignedIn() {
        return signedIn;
    }

    public void setSignedIn(boolean value) {
        signedIn = value;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean value) {
        revoked = value;
    }

    public boolean passwordMatches(String candidatePassword) {
        return password.equals(candidatePassword);
    }

}
