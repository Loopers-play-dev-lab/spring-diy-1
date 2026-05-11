package com.diy.framework.view;

public enum ViewType {
    JSP(".jsp"),
    REDIRECT("redirect:"),
    MUSTACHE(".mustache");

    private final String marker;

    public final String getMarker() {
        return this.marker;
    }

    ViewType(String marker) {
        this.marker = marker;
    }
}
