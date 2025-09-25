/*
 * Copyright (C) 2013-2025 Thomas Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tomakehurst.wiremock.core;

import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import java.util.List;

/** An interface for saving and removing stub mappings. */
public interface MappingsSaver {
  /**
   * Saves a list of stub mappings.
   *
   * @param stubMappings the stub mappings to save
   */
  void save(List<StubMapping> stubMappings);

  /**
   * Saves a single stub mapping.
   *
   * @param stubMapping the stub mapping to save
   */
  void save(StubMapping stubMapping);

  /**
   * Removes a single stub mapping.
   *
   * @param stubMapping the stub mapping to remove
   */
  void remove(StubMapping stubMapping);

  /** Removes all stub mappings. */
  void removeAll();
}
