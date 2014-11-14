package org.sonar.api.compat.internal;

import org.picocontainer.Startable;
import org.sonar.api.BatchComponent;
import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.batch.sensor.duplication.DuplicationGroup;
import org.sonar.api.platform.ComponentContainer;

import java.lang.reflect.Method;
import java.util.List;

@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class DuplicationCacheCompat implements BatchComponent, Startable {

  private Object originalDuplicationCache;
  private Method putMethod;
  private ComponentContainer container;

  public DuplicationCacheCompat(ComponentContainer container) {
    this.container = container;
  }

  @Override
  public final void start() {
    Class duplicationCacheClass;
    try {
      duplicationCacheClass = Class.forName("org.sonar.batch.duplication.DuplicationCache");
    } catch (Exception e) {
      throw new IllegalStateException("Unable to find class org.sonar.batch.duplication.DuplicationCache");
    }
    this.originalDuplicationCache = container.getComponentByType(duplicationCacheClass);
    try {
      putMethod = originalDuplicationCache.getClass().getDeclaredMethod("put", String.class, List.class);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to find method 'put' on DuplicationCache", e);
    }
  }

  @Override
  public final void stop() {
  }

  public DuplicationCacheCompat put(String effectiveKey, List<DuplicationGroup> groups) {
    try {
      putMethod.invoke(originalDuplicationCache, effectiveKey, groups);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to invoke method 'put' on BlockCache", e);
    }
    return this;
  }

}
