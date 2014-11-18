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

import com.google.common.base.Preconditions;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.sonar.api.compat.batch.sensor.SensorStorage;

import javax.annotation.Nullable;

public abstract class DefaultStorable<G> {

  protected transient final SensorStorage storage;
  private transient boolean saved = false;

  public DefaultStorable() {
    this.storage = null;
  }

  public DefaultStorable(@Nullable SensorStorage storage) {
    this.storage = storage;
  }

  public final G save() {
    Preconditions.checkNotNull(this.storage, "No persister on this object");
    Preconditions.checkState(!saved, "This object was already saved");
    doSave();
    this.saved = true;
    return (G) this;
  }

  protected abstract void doSave();

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

}
