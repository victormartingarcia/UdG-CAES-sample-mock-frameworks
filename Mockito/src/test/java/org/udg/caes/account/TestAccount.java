package org.udg.caes.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestAccount {

    @Mock
    Account acc1;
    @Mock
    Account acc2;

    @Mock
    AccountManager am;

    AccountService as;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        as = new AccountService();
        as.setAccountManager(am);
    }

    @Test
    void test() {

        when(am.findAccount("john")).thenReturn(acc1);
        when(am.findAccount("peter")).thenReturn(acc2);

        as.transfer("john", "peter", 100);

        verify(acc1).debit(100);
        verify(acc2).credit(100);
    }
}