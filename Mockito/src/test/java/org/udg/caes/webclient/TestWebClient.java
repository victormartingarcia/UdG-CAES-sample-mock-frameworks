package org.udg.caes.webclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestWebClient {

    @Mock
    ConnectionFactory cf;
    @Mock
    InputStream is;

    WebClient wc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        wc = new WebClient();
    }

    @Test
    public void test_read_exception() throws IOException {

        when(cf.getData()).thenReturn(is);
        when(is.read())
                .thenReturn((int) 'I')
                .thenThrow(new IOException());

        String result = wc.getContent(cf);

        assertNull(result);
    }

    @Test
    public void test_ok(){

        when(cf.getData()).thenReturn(new ByteArrayInputStream(new String("It works").getBytes()));

        String result = wc.getContent(cf);

        assertNotNull(result);
        assertEquals("It works", result);

    }

    @Test
    public void test_close_ok() throws IOException {

        when(cf.getData()).thenReturn(is);
        when(is.read())
                .thenReturn((int) 'I')
                .thenReturn((int) 't')
                .thenReturn((int) ' ')
                .thenReturn((int) 'w')
                .thenReturn((int) 'o')
                .thenReturn((int) 'r')
                .thenReturn((int) 'k')
                .thenReturn((int) 's')
                .thenReturn(-1);

        String result = wc.getContent(cf);

        assertNotNull(result);
        assertEquals("It works", result);

        verify(is).close();
    }

    @Test
    public void test_null_InputStream() {

        when(cf.getData()).thenReturn(null);

        String result = wc.getContent(cf);

        assertNull(result);
    }

}