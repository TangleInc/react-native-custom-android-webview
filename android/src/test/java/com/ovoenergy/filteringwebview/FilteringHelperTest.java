package com.ovoenergy.filteringwebview;

import com.ovoenergy.filteringwebview.FilteringHelper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilteringHelperTest {
    List<Object> filterUrls = new ArrayList<>();
    List<String> testData = Arrays.asList("duckduckgo.com", "help.duckduckgo.com");
    private FilteringHelper myHelper;

    @Before
    public void init() {
        filterUrls.addAll(testData);
        myHelper = new FilteringHelper(filterUrls);
    }

    @Test
    public void shouldInstatiateFilteringHelperWithArrayList() {
        Assert.assertEquals(testData, myHelper.getOpenInternallyHosts());
    }

    @Test
    public void shouldOpenInternally() {
        Assert.assertEquals("Whitelisted", true, myHelper.shouldOpenInternally("https://duckduckgo.com"));
        Assert.assertEquals("Subdomain", true, myHelper.shouldOpenInternally("https://help.duckduckgo.com"));
        Assert.assertEquals("With Path", true, myHelper.shouldOpenInternally("https://help.duckduckgo.com/asdgasdg/"));
    }

    @Test
    public void shouldReturnFalseFor() {
        Assert.assertEquals("Not whitelisted", false, myHelper.shouldOpenInternally("https://facebook.com"));
        Assert.assertEquals("Not whitelisted with subdomain", false, myHelper.shouldOpenInternally("https://m.facebook.com"));
        Assert.assertEquals("Subdomain check", false, myHelper.shouldOpenInternally("https://apps.facebook.com"));

        // @Note: we're checking host, not domain
        Assert.assertEquals("Should check host, not domain", false, myHelper.shouldOpenInternally("https://apps.duckduckgo.com"));
    }

}
