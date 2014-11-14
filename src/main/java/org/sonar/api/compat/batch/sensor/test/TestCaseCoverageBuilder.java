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
package org.sonar.api.compat.batch.sensor.test;

import org.sonar.api.compat.batch.sensor.internal.Builder;

import org.sonar.api.batch.fs.InputFile;

import java.util.List;

/**
 * Builder for {@link TestCaseCoverage}
 */
public interface TestCaseCoverageBuilder extends Builder<TestCaseCoverage> {

  /**
   * Set file where this test is located. Mandatory.
   */
  TestCaseCoverageBuilder testFile(InputFile testFile);

  /**
   * Set name of this test. Name is mandatory.
   */
  TestCaseCoverageBuilder testName(String name);

  /**
   * Set file covered by this test. Mandatory.
   */
  TestCaseCoverageBuilder cover(InputFile mainFile);

  /**
   * Set list of line numbers (1-based) covered by this test. Mandatory.
   */
  TestCaseCoverageBuilder onLines(List<Integer> lines);

  /**
   * Call this method only once when your are done with defining the test case coverage.
   */
  @Override
  TestCaseCoverage save();

}
