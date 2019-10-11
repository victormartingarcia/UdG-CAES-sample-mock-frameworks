package org.udg.caes.signin.userService;

import org.udg.caes.signin.userAccount.UserAccount;

public interface IUserService {
    UserAccount find(String accountId);
}
