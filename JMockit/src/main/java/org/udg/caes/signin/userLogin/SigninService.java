/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.udg.caes.signin.userLogin;

import org.udg.caes.signin.userAccount.UserAccount;
import org.udg.caes.signin.userService.IUserService;

import java.util.List;

public final class SigninService {
    private static final int MAX_LOGIN_ATTEMPTS = 5;

    private int loginAttemptsRemaining = MAX_LOGIN_ATTEMPTS;
    private String previousAccountId = null;
    private UserAccount account;

    private IUserService userService;

    public SigninService(IUserService us) {
        userService = us;
    }

    public void signin(String accountId, String password)
            throws UserAccountNotFoundException, UserAccountRevokedException, UserAccountSignedInException {
        account = userService.find(accountId);

        if (account == null) {
            throw new UserAccountNotFoundException();
        }

        if (account.passwordMatches(password)) {
            registerNewLogin();
        } else {
            handleFailedLoginAttempt(accountId);
        }
    }

    private void registerNewLogin() throws UserAccountRevokedException, UserAccountSignedInException {
        if (account.isSignedIn()) {
            throw new UserAccountSignedInException();
        }

        if (account.isRevoked()) {
            throw new UserAccountRevokedException();
        }

        account.setSignedIn(true);
        loginAttemptsRemaining = MAX_LOGIN_ATTEMPTS;
    }

    private void handleFailedLoginAttempt(String accountId) {
        if (previousAccountId == null || accountId.equals(previousAccountId)) {
            loginAttemptsRemaining--;
        } else {
            loginAttemptsRemaining = MAX_LOGIN_ATTEMPTS - 1;
        }

        previousAccountId = accountId;

        if (loginAttemptsRemaining == 0) {
            account.setRevoked(true);
            loginAttemptsRemaining = MAX_LOGIN_ATTEMPTS;
        }
    }


}
