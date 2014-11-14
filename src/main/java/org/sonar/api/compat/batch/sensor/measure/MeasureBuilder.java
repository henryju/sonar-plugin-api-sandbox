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
package org.sonar.api.compat.batch.sensor.measure;

import org.sonar.api.compat.batch.sensor.internal.Builder;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.measure.Metric;

import java.io.Serializable;

/**
 * Builder to create new Measure.
 */
public interface MeasureBuilder<G extends Serializable> extends Builder<Measure<G>> {

  /**
   * The file the measure belongs to.
   */
  MeasureBuilder<G> onFile(InputFile file);

  /**
   * Tell that the measure is global to the project.
   */
  MeasureBuilder<G> onProject();

  /**
   * Set the metric this measure belong to.
   */
  MeasureBuilder<G> forMetric(Metric<G> metric);

  /**
   * Value of the measure.
   */
  MeasureBuilder<G> withValue(G value);

  /**
   * Save the measure. It is not permitted so save several measures of the same metric on the same file/project.
   */
  @Override
  Measure<G> save();

}
