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

import com.google.common.base.Preconditions;
import org.sonar.api.BatchComponent;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.sensor.duplication.DuplicationGroup;
import org.sonar.api.compat.batch.sensor.SensorContext;
import org.sonar.api.compat.batch.sensor.SensorStorage;
import org.sonar.api.compat.batch.sensor.dependency.Dependency;
import org.sonar.api.compat.batch.sensor.dependency.internal.DefaultDependency;
import org.sonar.api.compat.batch.sensor.duplication.DuplicationBuilder;
import org.sonar.api.compat.batch.sensor.duplication.DuplicationTokenBuilder;
import org.sonar.api.compat.batch.sensor.duplication.internal.DefaultDuplicationBuilder;
import org.sonar.api.compat.batch.sensor.duplication.internal.DefaultTokenBuilder;
import org.sonar.api.compat.batch.sensor.highlighting.HighlightingBuilder;
import org.sonar.api.compat.batch.sensor.highlighting.internal.DefaultHighlightingBuilder;
import org.sonar.api.compat.batch.sensor.issue.IssueBuilder;
import org.sonar.api.compat.batch.sensor.issue.internal.DefaultIssue;
import org.sonar.api.compat.batch.sensor.measure.Measure;
import org.sonar.api.compat.batch.sensor.measure.internal.DefaultMeasure;
import org.sonar.api.compat.batch.sensor.symbol.SymbolReferences;
import org.sonar.api.compat.batch.sensor.symbol.internal.DefaultSymbolReferences;
import org.sonar.api.compat.batch.sensor.test.Coverage;
import org.sonar.api.compat.batch.sensor.test.TestCaseCoverage;
import org.sonar.api.compat.batch.sensor.test.TestCaseExecution;
import org.sonar.api.compat.batch.sensor.test.internal.DefaultCoverage;
import org.sonar.api.compat.batch.sensor.test.internal.DefaultTestCaseCoverage;
import org.sonar.api.compat.batch.sensor.test.internal.DefaultTestCaseExecution;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.File;
import org.sonar.api.source.Highlightable;
import org.sonar.duplications.internal.pmd.PmdBlockChunker;

import java.io.Serializable;
import java.util.List;

/**
 * Implements {@link SensorContext} but forward everything to {@link org.sonar.api.batch.SensorContext} for backward compatibility.
 * Will be dropped once old {@link Sensor} API is dropped.
 *
 */
public class SensorContextCompat implements SensorContext, BatchComponent {

  private final Settings settings;
  private final FileSystem fs;
  private final ActiveRules activeRules;
  private final BlockCacheCompat blockCache;
  private final DuplicationCacheCompat duplicationCache;
  private final org.sonar.api.batch.SensorContext sensorContext;
  private final ResourcePerspectives perspectives;
  private SensorStorage sensorStorage;

  public SensorContextCompat(org.sonar.api.batch.SensorContext sensorContext, ResourcePerspectives perspectives,
    Settings settings, FileSystem fs, ActiveRules activeRules, BlockCacheCompat blockCache,
    DuplicationCacheCompat duplicationCache, SensorStorage sensorStorage) {
    this.settings = settings;
    this.fs = fs;
    this.activeRules = activeRules;
    this.blockCache = blockCache;
    this.duplicationCache = duplicationCache;
    this.sensorContext = sensorContext;
    this.perspectives = perspectives;
    this.sensorStorage = sensorStorage;
  }

  @Override
  public Settings settings() {
    return settings;
  }

  @Override
  public FileSystem fileSystem() {
    return fs;
  }

  @Override
  public ActiveRules activeRules() {
    return activeRules;
  }

  @Override
  public <G extends Serializable> Measure<G> newMeasure() {
    return (Measure<G>) new DefaultMeasure<G>(sensorStorage);
  }

  @Override
  public IssueBuilder newIssue() {
    return new DefaultIssue(sensorStorage);
  }

  @Override
  public HighlightingBuilder highlightingBuilder(InputFile inputFile) {
    File f = File.create(inputFile.relativePath());
    f = sensorContext.getResource(f);
    Highlightable h = perspectives.as(Highlightable.class, f);
    org.sonar.api.source.Highlightable.HighlightingBuilder newHighlighting = h.newHighlighting();
    return new DefaultHighlightingBuilder(newHighlighting);
  }

  @Override
  public SymbolReferences newSymbolReferences() {
    return new DefaultSymbolReferences(sensorStorage);
  }

  @Override
  public DuplicationTokenBuilder duplicationTokenBuilder(InputFile inputFile) {
    PmdBlockChunker blockChunker = new PmdBlockChunker(getBlockSize(inputFile.language()));

    return new DefaultTokenBuilder(inputFile, blockCache, blockChunker);
  }

  @Override
  public DuplicationBuilder duplicationBuilder(InputFile inputFile) {
    return new DefaultDuplicationBuilder(inputFile);
  }

  @Override
  public void saveDuplications(InputFile inputFile, List<DuplicationGroup> duplications) {
    Preconditions.checkState(!duplications.isEmpty(), "Empty duplications");
    String effectiveKey = ((DefaultInputFile) inputFile).key();
    for (DuplicationGroup duplicationGroup : duplications) {
      Preconditions.checkState(effectiveKey.equals(duplicationGroup.originBlock().resourceKey()), "Invalid duplication group");
    }
    duplicationCache.put(effectiveKey, duplications);
  }

  private int getBlockSize(String languageKey) {
    int blockSize = settings.getInt("sonar.cpd." + languageKey + ".minimumLines");
    if (blockSize == 0) {
      blockSize = getDefaultBlockSize(languageKey);
    }
    return blockSize;
  }

  private static int getDefaultBlockSize(String languageKey) {
    if ("cobol".equals(languageKey)) {
      return 30;
    } else if ("abap".equals(languageKey) || "natur".equals(languageKey)) {
      return 20;
    } else {
      return 10;
    }
  }

  @Override
  public Coverage newCoverage() {
    return new DefaultCoverage(sensorStorage);
  }

  @Override
  public TestCaseExecution newTestCaseExecution() {
    return new DefaultTestCaseExecution(sensorStorage);
  }

  @Override
  public TestCaseCoverage newTestCaseCoverage() {
    return new DefaultTestCaseCoverage(sensorStorage);
  }

  @Override
  public Dependency newDependency() {
    return new DefaultDependency(sensorStorage);
  }

}
