package com.ovoenergy.customwebview;

import java.net.URI;
import java.util.Collections;
import java.util.List;

public class FilteringHelper {
    private List<Object> openInternallyHosts = Collections.emptyList();

    public FilteringHelper(List<Object> filterList) {
        this.openInternallyHosts = filterList;
    }

    public boolean shouldOpenInternally(String urlString) {
        if (this.openInternallyHosts.size() == 0) {
            return true;
        }
        URI uri = URI.create(urlString);

        return this.openInternallyHosts.contains(uri.getHost());
    }


    public List<Object> getOpenInternallyHosts() {
        return openInternallyHosts;
    }

    public void setOpenInternallyHosts(List<Object> openInternallyHosts) {
        this.openInternallyHosts = openInternallyHosts;
    }
}
