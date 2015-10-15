package com.example.izhang.collaborativedj;

import static org.junit.Assert.*;

import org.junit.Test;

import com.example.izhang.collaborativedj.login;
public class EnterCodeTest {

    @Test
    public void testCode() {
        String codeNull = null;
        String codeLong = "dsfafdafdfsdafasdfsadf";
        String codeEmpty = "";
        String codeSuccess = "dsfdsfs";

        assertEquals(login.checkCode(codeNull),false);
        assertEquals(login.checkCode(codeLong),false);
        assertEquals(login.checkCode(codeEmpty),false);
        assertEquals(login.checkCode(codeSuccess),true);

    }


}
