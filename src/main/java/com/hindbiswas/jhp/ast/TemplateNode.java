package com.hindbiswas.jhp.ast;

import java.util.ArrayList;
import java.util.List;

public
/* Template top-level */
class TemplateNode implements AstNode {
    public final List<TemplateElementNode> elements = new ArrayList<>();

    @Override
    public String toString() {
        return "Template" + elements;
    }

    public void add(TemplateElementNode node) {
        elements.add(node);
    }
}
