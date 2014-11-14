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
package org.sonar.api.compat.batch.sensor.symbol;

import org.sonar.api.compat.batch.sensor.internal.Builder;

import org.sonar.api.batch.fs.InputFile;

/**
 * Builder for {@link SymbolReferences}
 */
public interface SymbolReferencesBuilder extends Builder<SymbolReferences> {

  SymbolReferencesBuilder inFile(InputFile file);

  SymbolReferencesBuilder declaredAt(int startOffset, int endOffset);

  /**
   * Records that symbol is referenced at another location in the same file.
   * @param startOffset Starting offset of the place symbol is referenced. No need for end offset here since we assume it is of same length.
   */
  SymbolReferencesBuilder referencedAt(int startOffset);

  /**
   * Call this method only once when your are done with defining the references of the symbol.
   */
  @Override
  SymbolReferences save();

}
