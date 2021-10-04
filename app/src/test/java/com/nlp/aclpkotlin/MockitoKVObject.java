package com.nlp.aclpkotlin;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MockitoKVObject {
    @Mock
    KVObjectJava kv;

    @Test
    public void testObjectValidity(){
        when(kv.getValue()).thenReturn("1");
        assertEquals("1", kv.getValue());
    }
}