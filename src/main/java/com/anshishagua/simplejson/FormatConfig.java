package com.anshishagua.simplejson;

public class FormatConfig {
    public static final String INDENT_STRING = "  ";

    private final int indent;
    private final boolean shouldIndent;
    private final String indentString;
    private final boolean shouldIndentStartToken;

    public FormatConfig(boolean shouldIndent, int indent, String indentString, boolean shouldIndentStartToken) {
        this.shouldIndent = shouldIndent;
        this.indent = indent;
        this.indentString = indentString;
        this.shouldIndentStartToken = shouldIndentStartToken;
    }

    public FormatConfig(boolean shouldIndent, int indent, String indentString) {
        this(shouldIndent, indent, indentString, true);
    }

    public FormatConfig(boolean shouldIndent, int indent) {
        this(shouldIndent, indent, INDENT_STRING);
    }

    public boolean shouldIndent() {
        return shouldIndent;
    }

    public int getIndent() {
        return indent;
    }

    public String getIndentString() {
        return indentString;
    }

    public boolean shouldIndentStartToken() {
        return shouldIndentStartToken;
    }
}
