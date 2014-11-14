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

import javax.annotation.Nullable;

/**
 * Represents result of execution of a single test in a test file.
 * @since 5.0
 */
public interface TestCaseExecution {

  /**
   * Test execution status.
   */
  enum Status {
    OK, FAILURE, ERROR, SKIPPED;

    public static Status of(@Nullable String s) {
      return s == null ? null : valueOf(s.toUpperCase());
    }
  }

  /**
   * Test type.
   */
  enum TestType {
    UNIT, INTEGRATION;
  }

  /**
   * InputFile where this test is located.
   */
  InputFile testFile();

  /**
   * Duration in milliseconds
   */
  Long durationInMs();

  /**
   * Name of this test case.
   */
  String name();

  /**
   * Status of execution of the test.
   */
  Status status();

  /**
   * Message (usually in case of {@link Status#ERROR} or {@link Status#FAILURE}).
   */
  String message();

  /**
   * Type of test.
   */
  TestType type();

  /**
   * Stacktrace (usually in case of {@link Status#ERROR}).
   */
  String stackTrace();

}
