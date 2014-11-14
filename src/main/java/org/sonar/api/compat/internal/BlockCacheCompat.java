package org.sonar.api.compat.internal;

import org.picocontainer.Startable;
import org.sonar.api.BatchComponent;
import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.platform.ComponentContainer;
import org.sonar.duplications.block.FileBlocks;

import java.lang.reflect.Method;

@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class BlockCacheCompat implements BatchComponent, Startable {

  private Object originalBlockCache;
  private Method putMethod;
  private ComponentContainer container;

  public BlockCacheCompat(ComponentContainer container) {
    this.container = container;
  }

  @Override
  public final void start() {
    Class blockCacheClass;
    try {
      blockCacheClass = Class.forName("org.sonar.batch.duplication.BlockCache");
    } catch (Exception e) {
      throw new IllegalStateException("Unable to find class org.sonar.batch.duplication.BlockCache");
    }
    this.originalBlockCache = container.getComponentByType(blockCacheClass);
    try {
      putMethod = originalBlockCache.getClass().getDeclaredMethod("put", String.class, FileBlocks.class);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to find method 'put' on BlockCache", e);
    }
  }

  @Override
  public final void stop() {
  }

  public BlockCacheCompat put(String effectiveKey, FileBlocks blocks) {
    try {
      putMethod.invoke(originalBlockCache, effectiveKey, blocks);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to invoke method 'put' on BlockCache", e);
    }
    return this;
  }
}
