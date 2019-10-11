package org.udg.caes.signin;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.udg.caes.signin.userAccount.UserAccount;
import org.udg.caes.signin.userLogin.SigninService;
import org.udg.caes.signin.userLogin.UserAccountNotFoundException;
import org.udg.caes.signin.userLogin.UserAccountRevokedException;
import org.udg.caes.signin.userLogin.UserAccountSignedInException;
import org.udg.caes.signin.userService.IUserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAccountService_signin {

    @Mocked
    IUserService us;

    SigninService ss;

    @BeforeEach
    void setup() {
        ss = new SigninService(us);
    }

    @Test
    void test_ok(@Mocked UserAccount ua) throws UserAccountNotFoundException, UserAccountRevokedException, UserAccountSignedInException {

        new Expectations() {{
            us.find("id1");
            result = ua;
            ua.passwordMatches("pass");
            result = true;
            ua.isSignedIn();
            result = false;
            ua.isRevoked();
            result = false;
        }};

        ss.signin("id1", "pass");

        new Verifications() {{
            ua.setSignedIn(true);
            times = 1;
        }};
    }

    @Test
    void test_password_not_match(@Mocked UserAccount ua) throws UserAccountNotFoundException, UserAccountRevokedException, UserAccountSignedInException {

        new Expectations() {{
            us.find("id1");
            result = ua;
            ua.passwordMatches("pass");
            result = false;
        }};

        int attemptsBefore = Deencapsulation.getField(ss, "loginAttemptsRemaining");

        ss.signin("id1", "pass");

        int attemptsAfter = Deencapsulation.getField(ss, "loginAttemptsRemaining");

        new Verifications() {{
            ua.setRevoked(anyBoolean);
            times = 0;
            ua.setSignedIn(anyBoolean);
            times = 0;
        }};

        assertEquals(1, attemptsBefore - attemptsAfter);
    }


    @Test
    void test_account_not_found() throws UserAccountNotFoundException, UserAccountRevokedException, UserAccountSignedInException {

        new Expectations() {{
            us.find("id1");
            result = null;
        }};

        assertThrows(UserAccountNotFoundException.class,
                () -> ss.signin("id1", "pass"),
                "Acoount not fiund should rise exception");

    }

    @Test
    void test_is_signedin() throws UserAccountNotFoundException, UserAccountRevokedException, UserAccountSignedInException {

        UserAccount ua = new UserAccount("id1", "pass");
        ua.setSignedIn(true);

        new Expectations() {{
            us.find("id1");
            result = ua;
        }};

        assertThrows(UserAccountSignedInException.class,
                () -> ss.signin("id1", "pass"));

    }

    @Test
    void test_is_revoked() throws UserAccountNotFoundException, UserAccountRevokedException, UserAccountSignedInException {

        UserAccount ua = new UserAccount("id1", "pass");
        ua.setRevoked(true);

        new Expectations() {{
            us.find("id1");
            result = ua;
        }};

        assertThrows(UserAccountRevokedException.class,
                () -> ss.signin("id1", "pass"));

    }

    @Test
    void test_password_not_match_2_accounts() throws UserAccountNotFoundException, UserAccountRevokedException, UserAccountSignedInException {

        UserAccount ua1 = new UserAccount("id1", "pass1");
        UserAccount ua2 = new UserAccount("id2", "pass2");

        new Expectations() {{
            us.find("id1");
            result = ua1;
            us.find("id2");
            result = ua2;
        }};

        ss.signin("id1", "pass1");
        ss.signin("id2", "pass3");

    }

    @Test
    void test_ok_1_account_2_times() throws UserAccountNotFoundException, UserAccountRevokedException, UserAccountSignedInException {

        UserAccount ua1 = new UserAccount("id1", "pass1");

        new Expectations() {{
            us.find("id1");
            result = ua1;
        }};

        ss.signin("id1", "pass2");
        ss.signin("id1", "pass2");

    }

    @Test
    void test_ok_2_account_2_times() throws UserAccountNotFoundException, UserAccountRevokedException, UserAccountSignedInException {

        UserAccount ua1 = new UserAccount("id1", "pass1");
        UserAccount ua2 = new UserAccount("id2", "pass2");

        new Expectations() {{
            us.find("id1");
            result = ua1;
            us.find("id2");
            result = ua2;
        }};

        ss.signin("id1", "pass11");
        ss.signin("id2", "pass22");

    }

    @Test
    void test_ok_1_account_MAX_times(@Mocked UserAccount ua1) throws UserAccountNotFoundException, UserAccountRevokedException, UserAccountSignedInException {

        new Expectations() {{
            us.find("id1");
            result = ua1;
            ua1.passwordMatches(anyString);
            result = false;
        }};


        for (int i = 0; i < (Integer) Deencapsulation.getField(SigninService.class, "MAX_LOGIN_ATTEMPTS"); i++)
            ss.signin("id1", "pass2");

        new Verifications() {{
            ua1.setRevoked(true);
            times = 1;
        }};

    }

    @Test
    void test_ok_2_account_MAX1_times(@Mocked UserAccount ua1) throws UserAccountNotFoundException, UserAccountRevokedException, UserAccountSignedInException {

        new Expectations() {{
            us.find("id1");
            result = ua1;
            ua1.passwordMatches(anyString);
            result = false;
        }};


        for (int i = 0; i < (Integer) Deencapsulation.getField(SigninService.class, "MAX_LOGIN_ATTEMPTS") - 1; i++)
            ss.signin("id1", "pass2");

        ss.signin("id2", "pass2");

        for (int i = 0; i < (Integer) Deencapsulation.getField(SigninService.class, "MAX_LOGIN_ATTEMPTS") - 1; i++)
            ss.signin("id1", "pass2");

        new Verifications() {{
            ua1.setRevoked(true);
            times = 0;
        }};

    }


}
