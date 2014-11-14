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
package org.sonar.api.compat.internal;

import org.picocontainer.Startable;
import org.sonar.api.BatchComponent;
import org.sonar.api.batch.SonarIndex;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.compat.batch.sensor.SensorStorage;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.MetricFinder;
import org.sonar.api.resources.Project;

public class SensorContextCompatProvider implements BatchComponent, Startable {

  private final Settings settings;
  private final FileSystem fs;
  private final ActiveRules activeRules;
  private final BlockCacheCompat blockCache;
  private final DuplicationCacheCompat duplicationCache;
  private final MetricFinder metricFinder;
  private final Project project;
  private final ResourcePerspectives perspectives;
  private final SonarIndex sonarIndex;

  private static SensorContextCompatProvider current;

  public SensorContextCompatProvider(MetricFinder metricFinder, Project project, ResourcePerspectives perspectives,
    Settings settings, FileSystem fs, ActiveRules activeRules, BlockCacheCompat blockCache,
    DuplicationCacheCompat duplicationCache, SonarIndex sonarIndex) {
    this.settings = settings;
    this.fs = fs;
    this.activeRules = activeRules;
    this.blockCache = blockCache;
    this.duplicationCache = duplicationCache;
    this.metricFinder = metricFinder;
    this.project = project;
    this.perspectives = perspectives;
    this.sonarIndex = sonarIndex;

    current = this;
  }

  @Override
  public void start() {
    // Force early initialization of this component to make current singleton not null
  }

  @Override
  public void stop() {
  }

  public SensorContextCompat provide(org.sonar.api.batch.SensorContext oldContext) {
    SensorStorage storage = new SensorStorageCompat(oldContext, metricFinder, project, perspectives, sonarIndex);
    return new SensorContextCompat(oldContext, perspectives, settings, fs, activeRules, blockCache, duplicationCache, storage);
  }

  public static SensorContextCompatProvider getCurrent() {
    return current;
  }

}
