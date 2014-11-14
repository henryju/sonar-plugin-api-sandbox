/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2014 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.api.compat.batch.sensor.symbol.internal;

import com.google.common.base.Preconditions;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.compat.batch.sensor.SensorStorage;
import org.sonar.api.compat.batch.sensor.internal.DefaultStorable;
import org.sonar.api.compat.batch.sensor.symbol.SymbolReferences;
import org.sonar.api.compat.batch.sensor.symbol.SymbolReferencesBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

public class DefaultSymbolReferences extends DefaultStorable<SymbolReferences> implements SymbolReferences, SymbolReferencesBuilder {

  private InputFile file;
  private int startOffset = -1;
  private int endOffset = -1;
  private Set<Integer> references = new LinkedHashSet<Integer>();

  public DefaultSymbolReferences() {
    super(null);
  }

  public DefaultSymbolReferences(SensorStorage storage) {
    super(storage);
  }

  @Override
  public InputFile file() {
    return this.file;
  }

  @Override
  public DefaultSymbolReferences inFile(InputFile file) {
    Preconditions.checkState(this.file == null, "inFile already called");
    Preconditions.checkNotNull(file, "File can't be null");
    this.file = file;
    return this;
  }

  @Override
  public int declarationStartOffset() {
    return startOffset;
  }

  @Override
  public int declarationEndOffset() {
    return endOffset;
  }

  @Override
  public DefaultSymbolReferences declaredAt(int startOffset, int endOffset) {
    Preconditions.checkState(this.startOffset == -1, "declaredAt already called");
    Preconditions.checkArgument(startOffset >= 0, "startOffset should be >= 0");
    Preconditions.checkArgument(endOffset >= 0, "endOffset should be >= 0");
    this.startOffset = startOffset;
    this.endOffset = endOffset;
    return this;
  }

  @Override
  public int[] referencesStartOffsets() {
    int[] result = new int[references.size()];
    int idx = 0;
    for (Integer i : references) {
      result[idx++] = i;
    }
    return result;
  }

  @Override
  public DefaultSymbolReferences referencedAt(int startOffset) {
    Preconditions.checkArgument(!references.contains(startOffset), "reference already registered");
    Preconditions.checkArgument(startOffset < this.startOffset || startOffset >= this.endOffset, "reference overlap symbol declaration");
    references.add(startOffset);
    return this;
  }

  @Override
  public void doSave() {
    Preconditions.checkState(this.file != null, "source file is mandatory on symbol reference");
    Preconditions.checkState(this.startOffset != -1, "declaredAt was not called");
    Preconditions.checkState(this.references.isEmpty(), "symbol has no reference");
    storage.store(this);
  }

  /**
   * For testing only.
   */

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DefaultSymbolReferences that = (DefaultSymbolReferences) o;
    return file.equals(that.file) && startOffset == that.startOffset && endOffset == that.endOffset;
  }

  @Override
  public int hashCode() {
    return file.hashCode() * 27 + startOffset;
  }

}
