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
package org.sonar.api.compat.batch.sensor.internal;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.measure.Metric;
import org.sonar.api.compat.batch.sensor.SensorDescriptor;

import java.util.Arrays;
import java.util.Collection;

public class DefaultSensorDescriptor implements SensorDescriptor {

  private String name;
  private Metric<?>[] dependsOn = new Metric<?>[0];
  private Metric<?>[] provides = new Metric<?>[0];
  private String[] languages = new String[0];
  private InputFile.Type[] types = new InputFile.Type[0];
  private String[] ruleRepositories = new String[0];

  public String name() {
    return name;
  }

  public Metric[] dependsOn() {
    return dependsOn;
  }

  public Metric[] provides() {
    return provides;
  }

  public Collection<String> languages() {
    return Arrays.asList(languages);
  }

  public Collection<InputFile.Type> types() {
    return Arrays.asList(types);
  }

  public Collection<String> ruleRepositories() {
    return Arrays.asList(ruleRepositories);
  }

  @Override
  public DefaultSensorDescriptor name(String name) {
    this.name = name;
    return this;
  }

  @Override
  public DefaultSensorDescriptor dependsOn(Metric<?>... metrics) {
    this.dependsOn = metrics;
    return this;
  }

  @Override
  public DefaultSensorDescriptor provides(Metric<?>... metrics) {
    this.provides = metrics;
    return this;
  }

  @Override
  public DefaultSensorDescriptor workOnLanguages(String... languageKeys) {
    this.languages = languageKeys;
    return this;
  }

  @Override
  public DefaultSensorDescriptor workOnFileTypes(InputFile.Type... types) {
    this.types = types;
    return this;
  }

  @Override
  public DefaultSensorDescriptor createIssuesForRuleRepositories(String... repositoryKeys) {
    this.ruleRepositories = repositoryKeys;
    return this;
  }

}
