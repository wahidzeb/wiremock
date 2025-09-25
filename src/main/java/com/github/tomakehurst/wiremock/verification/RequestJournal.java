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
package com.github.tomakehurst.wiremock.verification;

import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The request journal, which stores and queries requests that have been received by WireMock.
 *
 * <p>This interface defines the API for the request journal.
 */
public interface RequestJournal {

  /**
   * Counts the number of requests that match the specified request pattern.
   *
   * @param requestPattern the request pattern to match
   * @return the number of matching requests
   */
  int countRequestsMatching(RequestPattern requestPattern);

  /**
   * Gets the requests that match the specified request pattern.
   *
   * @param requestPattern the request pattern to match
   * @return a list of matching requests
   */
  List<LoggedRequest> getRequestsMatching(RequestPattern requestPattern);

  /**
   * Gets all serve events.
   *
   * @return a list of all serve events
   */
  List<ServeEvent> getAllServeEvents();

  /**
   * Gets a single serve event by its ID.
   *
   * @param id the ID of the serve event to get
   * @return the serve event, or empty if not found
   */
  Optional<ServeEvent> getServeEvent(UUID id);

  /** Resets the request journal. */
  void reset();

  /**
   * Called when a request has been received.
   *
   * @param serveEvent the serve event
   */
  void requestReceived(ServeEvent serveEvent);

  /**
   * Called when a request has been completed.
   *
   * @param serveEvent the serve event
   */
  void serveCompleted(ServeEvent serveEvent);

  /**
   * Removes a serve event by its ID.
   *
   * @param eventId the ID of the serve event to remove
   */
  void removeEvent(UUID eventId);

  /**
   * Removes all serve events that match the specified request pattern.
   *
   * @param requestPattern the request pattern to match
   * @return a list of the removed serve events
   */
  List<ServeEvent> removeEventsMatching(RequestPattern requestPattern);

  /**
   * Removes all serve events for stubs that match the specified metadata pattern.
   *
   * @param metadataPattern the metadata pattern to match
   * @return a list of the removed serve events
   */
  List<ServeEvent> removeServeEventsForStubsMatchingMetadata(StringValuePattern metadataPattern);
}
