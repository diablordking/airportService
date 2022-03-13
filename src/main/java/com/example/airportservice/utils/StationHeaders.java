package com.example.airportservice.utils;

public enum StationHeaders {
    NAME("Name"), LAST_MODIFIED("Last modified"), SIZE("Size");

    public final String header;

    StationHeaders() {
        this.header = null;
    }

    StationHeaders(String header) {
        this.header = header;
    }

    public String getHeader() {
        return this.header;
    }
}
