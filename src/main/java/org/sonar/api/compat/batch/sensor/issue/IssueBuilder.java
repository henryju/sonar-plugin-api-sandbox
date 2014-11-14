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
package org.sonar.api.compat.batch.sensor.issue;

import org.sonar.api.compat.batch.sensor.internal.Builder;

import org.sonar.api.batch.fs.InputDir;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.compat.batch.sensor.issue.Issue.Severity;
import org.sonar.api.rule.RuleKey;

import javax.annotation.Nullable;

/**
 * Builder for {@link Issue}
 */
public interface IssueBuilder extends Builder<Issue> {

  /**
   * The {@link RuleKey} of the issue.
   */
  IssueBuilder ruleKey(RuleKey ruleKey);

  /**
   * The {@link InputFile} the issue belongs to. For global issues call {@link #onProject()}.
   */
  IssueBuilder onFile(InputFile file);

  /**
   * The {@link InputDir} the issue belongs to. For global issues call {@link #onProject()}.
   */
  IssueBuilder onDir(InputDir inputDir);

  /**
   * Tell that the issue is global to the project.
   */
  IssueBuilder onProject();

  /**
   * Line of the issue. Only available for {@link #onFile(InputFile)} issues. 
   * If no line is specified it means that issue is global to the file.
   */
  IssueBuilder atLine(int line);

  /**
   * Effort to fix the issue.
   */
  IssueBuilder effortToFix(@Nullable Double effortToFix);

  /**
   * Message of the issue.
   */
  IssueBuilder message(String message);

  /**
   * Override severity of the issue.
   * Setting a null value or not calling this method means to use severity configured in quality profile.
   */
  IssueBuilder overrideSeverity(@Nullable Severity severity);

  /**
   * Save the issue. If rule key is unknow or rule not enabled in the current quality profile then a warning is logged but no exception
   * is thrown.
   */
  @Override
  Issue save();

}
