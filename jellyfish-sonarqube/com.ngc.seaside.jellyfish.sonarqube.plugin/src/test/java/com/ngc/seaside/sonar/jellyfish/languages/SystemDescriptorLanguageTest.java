package com.ngc.seaside.sonar.jellyfish.languages;

import org.sonar.api.resources.AbstractLanguage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class SystemDescriptorLanguageTest {
    private static final String NAME = "System Descriptor";
    private static final String KEY = "systemdescriptor";
    private static final String[] FILE_SUFFIXES = { ".sd" };

    private SystemDescriptorLanguage language;

    @Before
    public void beforeTests() {
        language = new SystemDescriptorLanguage();
    }

    @Test
    public void providesCorrectFileSuffixes() {
        assertArrayEquals(language.getFileSuffixes(), FILE_SUFFIXES);
    }

    @Test
    public void definesCorrectName() {
        assertEquals(language.getName(), NAME);
    }

    @Test
    public void definesCorrectKey() {
        assertEquals(language.getKey(), KEY);
    }
}
