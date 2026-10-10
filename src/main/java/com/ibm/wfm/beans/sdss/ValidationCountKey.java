package com.ibm.wfm.beans.sdss;

public record ValidationCountKey(
        String attributeName,
        String value,
        String reason
) {}