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

import org.sonar.api.batch.fs.InputFile;

import java.util.List;

/**
 * Represents line coverage produced by a single test in a test file on a single main file.
 */
public interface TestCaseCoverage {

  /**
   * InputFile where this test is located.
   */
  InputFile testFile();

  /**
   * Name of this test case.
   */
  String testName();

  /**
   * InputFile covered by this test.
   */
  InputFile coveredFile();

  /**
   * List of line numbers (1-based) covered by this test.
   */
  List<Integer> coveredLines();

}
