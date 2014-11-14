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
package org.sonar.api.compat.batch.sensor;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.sensor.duplication.DuplicationGroup;
import org.sonar.api.compat.batch.sensor.dependency.Dependency;
import org.sonar.api.compat.batch.sensor.duplication.DuplicationBuilder;
import org.sonar.api.compat.batch.sensor.duplication.DuplicationTokenBuilder;
import org.sonar.api.compat.batch.sensor.highlighting.HighlightingBuilder;
import org.sonar.api.compat.batch.sensor.issue.Issue;
import org.sonar.api.compat.batch.sensor.issue.IssueBuilder;
import org.sonar.api.compat.batch.sensor.measure.Measure;
import org.sonar.api.compat.batch.sensor.symbol.SymbolReferences;
import org.sonar.api.compat.batch.sensor.test.Coverage;
import org.sonar.api.compat.batch.sensor.test.TestCaseCoverage;
import org.sonar.api.compat.batch.sensor.test.TestCaseExecution;
import org.sonar.api.config.Settings;

import java.io.Serializable;
import java.util.List;

/**
 * See {@link Sensor#execute(SensorContext)}
 */
public interface SensorContext {

  /**
   * Get settings of the current project.
   */
  Settings settings();

  /**
   * Get filesystem of the current project.
   */
  FileSystem fileSystem();

  /**
   * Get list of active rules.
   */
  ActiveRules activeRules();

  // ----------- MEASURES --------------

  /**
   * Fluent builder to create a new {@link Measure}. Don't forget to call {@link Measure#save()} once all parameters are provided.
   */
  <G extends Serializable> Measure<G> newMeasure();

  // ----------- ISSUES --------------

  /**
   * Fluent builder to create a new {@link Issue}. Don't forget to call {@link IssueBuilder#save()} once all parameters are provided.
   */
  IssueBuilder newIssue();

  // ------------ HIGHLIGHTING ------------

  /**
   * Builder to define highlighting of a file.
   */
  HighlightingBuilder highlightingBuilder(InputFile inputFile);

  // ------------ SYMBOL REFERENCES ------------

  /**
   * Builder to define symbol references in a file.
   */
  SymbolReferences newSymbolReferences();

  // ------------ DUPLICATIONS ------------

  /**
   * Builder to define tokens in a file. Tokens are used to compute duplication using default SonarQube engine.
   */
  DuplicationTokenBuilder duplicationTokenBuilder(InputFile inputFile);

  /**
   * Builder to manually define duplications in a file. When duplication are manually computed then
   * no need to use {@link #duplicationTokenBuilder(InputFile)}.
   */
  DuplicationBuilder duplicationBuilder(InputFile inputFile);

  /**
   * Register all duplications of an {@link InputFile}. Use {@link #duplicationBuilder(InputFile)} to create
   * list of duplications.
   */
  void saveDuplications(InputFile inputFile, List<DuplicationGroup> duplications);

  // ------------ TESTS ------------

  /**
   * Create a new coverage report.
   * Don't forget to call {@link Coverage#save()} once all parameters are provided.
   */
  Coverage newCoverage();

  /**
   * Create a new test case execution report.
   * Don't forget to call {@link TestCaseExecution#save()} once all parameters are provided.
   */
  TestCaseExecution newTestCaseExecution();

  /**
   * Create a new test case coverage report.
   * Don't forget to call {@link TestCaseCoverage#save()} once all parameters are provided.
   */
  TestCaseCoverage newTestCaseCoverage();

  // ------------ DEPENDENCIES ------------

  /**
   * Create a new dependency.
   * Don't forget to call {@link Dependency#save()} once all parameters are provided.
   */
  Dependency newDependency();

}
