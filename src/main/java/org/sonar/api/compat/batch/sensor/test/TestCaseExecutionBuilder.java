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
import org.sonar.api.compat.batch.sensor.test.TestCaseExecution.Status;

/**
 * Builder for {@link TestCaseExecution}
 */
public interface TestCaseExecutionBuilder extends Builder<TestCaseExecution> {

  /**
   * Set file where this test is located. Mandatory.
   */
  TestCaseExecutionBuilder inTestFile(InputFile testFile);

  /**
   * Duration in milliseconds
   */
  TestCaseExecutionBuilder durationInMs(long duration);

  /**
   * Set name of this test. Name is mandatory.
   */
  TestCaseExecutionBuilder name(String name);

  /**
   * Status of execution of the test.
   */
  TestCaseExecutionBuilder status(Status status);

  /**
   * Message (usually in case of {@link Status#ERROR} or {@link Status#FAILURE}).
   */
  TestCaseExecutionBuilder message(String message);

  /**
   * Type of test.
   */
  TestCaseExecutionBuilder ofType(TestCaseExecution.TestType type);

  /**
   * Set stacktrace (usually in case of {@link Status#ERROR}).
   */
  TestCaseExecutionBuilder stackTrace(String stackTrace);

  /**
   * Call this method only once when your are done with defining the test case execution.
   */
  @Override
  TestCaseExecution save();

}
