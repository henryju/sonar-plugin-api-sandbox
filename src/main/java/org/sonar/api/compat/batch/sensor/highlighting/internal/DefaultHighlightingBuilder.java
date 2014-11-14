package org.sonar.api.compat.batch.sensor.highlighting.internal;

import com.google.common.base.Preconditions;
import org.sonar.api.compat.batch.sensor.highlighting.HighlightingBuilder;
import org.sonar.api.compat.batch.sensor.highlighting.HighlightingStyle;

public class DefaultHighlightingBuilder implements HighlightingBuilder {

  private boolean done = false;
  private org.sonar.api.source.Highlightable.HighlightingBuilder oldBuilder;

  public DefaultHighlightingBuilder(org.sonar.api.source.Highlightable.HighlightingBuilder oldBuilder) {
    this.oldBuilder = oldBuilder;
  }

  @Override
  public HighlightingBuilder highlight(int startOffset, int endOffset, HighlightingStyle typeOfText) {
    Preconditions.checkState(!done, "done() already called");
    oldBuilder.highlight(startOffset, endOffset, typeOfText.cssClass());
    return this;
  }

  @Override
  public void done() {
    Preconditions.checkState(!done, "done() already called");
    oldBuilder.done();
    this.done = true;
  }
}
