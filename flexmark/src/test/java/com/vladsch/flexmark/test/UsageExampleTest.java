package com.vladsch.flexmark.test;

import com.vladsch.flexmark.ast.*;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.ast.NodeVisitor;
import com.vladsch.flexmark.util.ast.VisitHandler;
import com.vladsch.flexmark.util.ast.Visitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UsageExampleTest {

    @Test
    public void parseAndRender() {
        Parser parser = Parser.builder().build();
        Node document = parser.parse("This is *Sparta*");
        HtmlRenderer renderer = HtmlRenderer.builder().escapeHtml(true).build();
        assertEquals("<p>This is <em>Sparta</em></p>\n", renderer.render(document));
    }

    @Test
    public void visitor() {
        Parser parser = Parser.builder().build();
        Node node = parser.parse("Example\n=======\n\nSome more text");

        WordCountVisitor visitor = new WordCountVisitor();
        visitor.countWords(node);
        assertEquals(4, visitor.wordCount);
    }

    class WordCountVisitor {
        int wordCount = 0;

        private final NodeVisitor myVisitor;

        public WordCountVisitor() {
            myVisitor = new NodeVisitor(
                    new VisitHandler<Text>(Text.class, new Visitor<Text>() {
                        @Override
                        public void visit(Text text) {
                            WordCountVisitor.this.visit(text);
                        }
                    })
            );
        }

        public void countWords(Node node) {
            myVisitor.visit(node);
        }

        private void visit(Text text) {
            // This is called for all Text nodes. Override other visit methods for other node types.

            // Count words (this is just an example, don't actually do it this way for various reasons).
            wordCount += text.getChars().toString().split("\\W+").length;

            // Descend into children (could be omitted in this case because Text nodes don't have children).
            myVisitor.visitChildren(text);
        }
    }
}
