/*
 * Copyright (C) 2011-2025 Thomas Akehurst
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

import com.github.tomakehurst.wiremock.admin.model.*;
import com.github.tomakehurst.wiremock.global.GlobalSettings;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.github.tomakehurst.wiremock.recording.RecordSpec;
import com.github.tomakehurst.wiremock.recording.RecordSpecBuilder;
import com.github.tomakehurst.wiremock.recording.RecordingStatusResult;
import com.github.tomakehurst.wiremock.recording.SnapshotRecordResult;
import com.github.tomakehurst.wiremock.stubbing.StubImport;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.verification.*;
import java.util.UUID;

/**
 * The administrative interface for WireMock.
 *
 * <p>This interface defines all the operations that can be performed on a WireMock server, such as
 * creating, editing, and deleting stub mappings, and resetting the server state.
 */
public interface Admin {

  /**
   * Adds a new stub mapping.
   *
   * @param stubMapping the stub mapping to add
   */
  void addStubMapping(StubMapping stubMapping);

  /**
   * Edits an existing stub mapping.
   *
   * @param stubMapping the stub mapping to edit
   */
  void editStubMapping(StubMapping stubMapping);

  /**
   * Removes a stub mapping.
   *
   * @param stubbMapping the stub mapping to remove
   */
  void removeStubMapping(StubMapping stubbMapping);

  /**
   * Removes a stub mapping by its ID.
   *
   * @param id the ID of the stub mapping to remove
   */
  void removeStubMapping(UUID id);

  /**
   * Lists all stub mappings.
   *
   * @return a list of all stub mappings
   */
  ListStubMappingsResult listAllStubMappings();

  /**
   * Gets a single stub mapping by its ID.
   *
   * @param id the ID of the stub mapping to get
   * @return the stub mapping, or an empty result if not found
   */
  SingleStubMappingResult getStubMapping(UUID id);

  /** Saves all stub mappings to the backing store. */
  void saveMappings();

  /** Resets the request journal. */
  void resetRequests();

  /** Resets all scenarios. */
  void resetScenarios();

  /** Resets the stub mappings. */
  void resetMappings();

  /**
   * Resets the entire server state, including stub mappings, scenarios, and the request journal.
   */
  void resetAll();

  /** Resets to the default stub mappings. */
  void resetToDefaultMappings();

  /**
   * Gets all serve events.
   *
   * @return all serve events
   */
  GetServeEventsResult getServeEvents();

  /**
   * Gets serve events matching the specified query.
   *
   * @param query the query to match
   * @return the matching serve events
   */
  GetServeEventsResult getServeEvents(ServeEventQuery query);

  /**
   * Gets a single served stub by its ID.
   *
   * @param id the ID of the served stub to get
   * @return the served stub, or an empty result if not found
   */
  SingleServedStubResult getServedStub(UUID id);

  /**
   * Counts the number of requests matching the specified request pattern.
   *
   * @param requestPattern the request pattern to match
   * @return the number of matching requests
   */
  VerificationResult countRequestsMatching(RequestPattern requestPattern);

  /**
   * Finds all requests matching the specified request pattern.
   *
   * @param requestPattern the request pattern to match
   * @return a list of matching requests
   */
  FindRequestsResult findRequestsMatching(RequestPattern requestPattern);

  /**
   * Finds all unmatched requests.
   *
   * @return a list of all unmatched requests
   */
  FindRequestsResult findUnmatchedRequests();

  /**
   * Removes a serve event by its ID.
   *
   * @param eventId the ID of the serve event to remove
   */
  void removeServeEvent(UUID eventId);

  /**
   * Removes all serve events matching the specified request pattern.
   *
   * @param requestPattern the request pattern to match
   * @return the removed serve events
   */
  FindServeEventsResult removeServeEventsMatching(RequestPattern requestPattern);

  /**
   * Removes all serve events for stubs matching the specified metadata pattern.
   *
   * @param pattern the metadata pattern to match
   * @return the removed serve events
   */
  FindServeEventsResult removeServeEventsForStubsMatchingMetadata(StringValuePattern pattern);

  /**
   * Finds the top near misses for the specified logged request.
   *
   * @param loggedRequest the logged request
   * @return the top near misses
   */
  FindNearMissesResult findTopNearMissesFor(LoggedRequest loggedRequest);

  /**
   * Finds the top near misses for the specified request pattern.
   *
   * @param requestPattern the request pattern
   * @return the top near misses
   */
  FindNearMissesResult findTopNearMissesFor(RequestPattern requestPattern);

  /**
   * Finds the near misses for all unmatched requests.
   *
   * @return the near misses for all unmatched requests
   */
  FindNearMissesResult findNearMissesForUnmatchedRequests();

  /**
   * Gets all scenarios.
   *
   * @return all scenarios
   */
  GetScenariosResult getAllScenarios();

  /**
   * Resets a scenario by its name.
   *
   * @param name the name of the scenario to reset
   */
  void resetScenario(String name);

  /**
   * Sets the state of a scenario.
   *
   * @param name the name of the scenario to set
   * @param state the new state of the scenario
   */
  void setScenarioState(String name, String state);

  /**
   * Updates the global settings.
   *
   * @param settings the new global settings
   */
  void updateGlobalSettings(GlobalSettings settings);

  /**
   * Takes a snapshot of the current request journal and returns the generated stub mappings.
   *
   * @return the generated stub mappings
   */
  SnapshotRecordResult snapshotRecord();

  /**
   * Takes a snapshot of the current request journal and returns the generated stub mappings.
   *
   * @param spec the recording specification
   * @return the generated stub mappings
   */
  SnapshotRecordResult snapshotRecord(RecordSpec spec);

  /**
   * Takes a snapshot of the current request journal and returns the generated stub mappings.
   *
   * @param spec the recording specification builder
   * @return the generated stub mappings
   */
  SnapshotRecordResult snapshotRecord(RecordSpecBuilder spec);

  /**
   * Starts recording requests.
   *
   * @param targetBaseUrl the base URL of the target server
   */
  void startRecording(String targetBaseUrl);

  /**
   * Starts recording requests.
   *
   * @param spec the recording specification
   */
  void startRecording(RecordSpec spec);

  /**
   * Starts recording requests.
   *
   * @param recordSpec the recording specification builder
   */
  void startRecording(RecordSpecBuilder recordSpec);

  /**
   * Stops recording requests and returns the generated stub mappings.
   *
   * @return the generated stub mappings
   */
  SnapshotRecordResult stopRecording();

  /**
   * Gets the current recording status.
   *
   * @return the current recording status
   */
  RecordingStatusResult getRecordingStatus();

  /**
   * Gets the options for this WireMock server.
   *
   * @return the options
   */
  Options getOptions();

  /** Shuts down the server. */
  void shutdownServer();

  /**
   * Finds all stub mappings that have not been matched.
   *
   * @return a list of all unmatched stub mappings
   */
  ListStubMappingsResult findUnmatchedStubs();

  /**
   * Finds all stubs by their metadata.
   *
   * @param pattern the metadata pattern to match
   * @return a list of matching stubs
   */
  ListStubMappingsResult findAllStubsByMetadata(StringValuePattern pattern);

  /**
   * Removes all stubs by their metadata.
   *
   * @param pattern the metadata pattern to match
   */
  void removeStubsByMetadata(StringValuePattern pattern);

  /**
   * Imports stubs from the specified import object.
   *
   * @param stubImport the import object
   */
  void importStubs(StubImport stubImport);

  /**
   * Gets the global settings.
   *
   * @return the global settings
   */
  GetGlobalSettingsResult getGlobalSettings();
}
