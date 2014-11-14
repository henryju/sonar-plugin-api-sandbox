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
package org.sonar.api.compat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.measure.Metric;
import org.sonar.api.compat.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.compat.internal.AnalyzerOptimizer;
import org.sonar.api.compat.internal.SensorContextCompatProvider;
import org.sonar.api.resources.Project;

import java.util.Arrays;
import java.util.List;

/**
 * Make your sensor extends this class to be able to use sandbox features of Sensor API.
 */
public abstract class SensorCompat implements org.sonar.api.batch.Sensor, org.sonar.api.compat.batch.sensor.Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(SensorCompat.class);

  private DefaultSensorDescriptor descriptor;

  public SensorCompat() {
    this.descriptor = new DefaultSensorDescriptor();
    describe(descriptor);
  }

  @DependedUpon
  public final List<Metric> provides() {
    return Arrays.asList(descriptor.provides());
  }

  @DependsUpon
  public final List<Metric> depends() {
    return Arrays.asList(descriptor.dependsOn());
  }

  @Override
  public final boolean shouldExecuteOnProject(Project project) {
    return AnalyzerOptimizer.getCurrent().shouldExecute(descriptor);
  }

  @Override
  public final void analyse(Project module, org.sonar.api.batch.SensorContext context) {
    execute(SensorContextCompatProvider.getCurrent().provide(context));
  }

  @Override
  public final String toString() {
    return descriptor.name() + (LOG.isDebugEnabled() ? " (wrapped)" : "");
  }
}
