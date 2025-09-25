/*
 * Copyright (C) 2014-2025 Thomas Akehurst
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

/**
 * An interface for injecting faults into responses.
 *
 * <p>This interface defines methods for injecting different kinds of faults into the response, such
 * as resetting the connection or sending a malformed response chunk.
 */
public interface FaultInjector {

  /** Resets the connection by peer. */
  void connectionResetByPeer();

  /** Sends an empty response and closes the connection. */
  void emptyResponseAndCloseConnection();

  /** Sends a malformed response chunk. */
  void malformedResponseChunk();

  /** Sends random data and closes the connection. */
  void randomDataAndCloseConnection();
}
