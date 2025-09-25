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
package com.github.tomakehurst.wiremock.client;

import com.github.tomakehurst.wiremock.common.Metadata;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ServeEventListener;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.*;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * A builder for creating stub mappings.
 *
 * <p>This interface defines the fluent API for building stub mappings.
 *
 * @see BasicMappingBuilder
 * @see ScenarioMappingBuilder
 */
public interface MappingBuilder {

  /**
   * Sets the scheme to match on.
   *
   * @param scheme the scheme
   * @return this builder
   */
  MappingBuilder withScheme(String scheme);

  /**
   * Sets the host to match on.
   *
   * @param hostPattern the host pattern
   * @return this builder
   */
  MappingBuilder withHost(StringValuePattern hostPattern);

  /**
   * Sets the port to match on.
   *
   * @param port the port
   * @return this builder
   */
  MappingBuilder withPort(int port);

  /**
   * Sets the client IP to match on.
   *
   * @param hostPattern the client IP pattern
   * @return this builder
   */
  MappingBuilder withClientIp(StringValuePattern hostPattern);

  /**
   * Sets the priority of this stub mapping.
   *
   * @param priority the priority
   * @return this builder
   */
  MappingBuilder atPriority(Integer priority);

  /**
   * Sets a header to match on.
   *
   * @param key the header key
   * @param headerPattern the header value pattern
   * @return this builder
   */
  MappingBuilder withHeader(String key, StringValuePattern headerPattern);

  /**
   * Sets a multi-value header to match on.
   *
   * @param key the header key
   * @param headerPattern the header value pattern
   * @return this builder
   */
  MappingBuilder withHeader(String key, MultiValuePattern headerPattern);

  /**
   * Sets a path parameter to match on.
   *
   * @param name the path parameter name
   * @param pattern the path parameter value pattern
   * @return this builder
   */
  MappingBuilder withPathParam(String name, StringValuePattern pattern);

  /**
   * Sets a query parameter to match on.
   *
   * @param key the query parameter key
   * @param queryParamPattern the query parameter value pattern
   * @return this builder
   */
  MappingBuilder withQueryParam(String key, StringValuePattern queryParamPattern);

  /**
   * Sets a multi-value query parameter to match on.
   *
   * @param key the query parameter key
   * @param multiValueQueryParamPattern the query parameter value pattern
   * @return this builder
   */
  MappingBuilder withQueryParam(String key, MultiValuePattern multiValueQueryParamPattern);

  /**
   * Sets a form parameter to match on.
   *
   * @param key the form parameter key
   * @param formParamPattern the form parameter value pattern
   * @return this builder
   */
  MappingBuilder withFormParam(String key, StringValuePattern formParamPattern);

  /**
   * Sets a multi-value form parameter to match on.
   *
   * @param key the form parameter key
   * @param multiValueFormParamPattern the form parameter value pattern
   * @return this builder
   */
  MappingBuilder withFormParam(String key, MultiValuePattern multiValueFormParamPattern);

  /**
   * Sets the form parameters to match on.
   *
   * @param formParams the form parameters
   * @return this builder
   */
  MappingBuilder withFormParams(Map<String, MultiValuePattern> formParams);

  /**
   * Sets the query parameters to match on.
   *
   * @param queryParams the query parameters
   * @return this builder
   */
  MappingBuilder withQueryParams(Map<String, StringValuePattern> queryParams);

  /**
   * Sets the request body to match on.
   *
   * @param bodyPattern the request body pattern
   * @return this builder
   */
  MappingBuilder withRequestBody(ContentPattern<?> bodyPattern);

  /**
   * Sets the multipart request body to match on.
   *
   * @param multipartPatternBuilder the multipart pattern builder
   * @return this builder
   */
  MappingBuilder withMultipartRequestBody(MultipartValuePatternBuilder multipartPatternBuilder);

  /**
   * Sets the scenario for this stub mapping.
   *
   * @param scenarioName the scenario name
   * @return a scenario mapping builder
   */
  ScenarioMappingBuilder inScenario(String scenarioName);

  /**
   * Sets the ID of this stub mapping.
   *
   * @param id the ID
   * @return this builder
   */
  MappingBuilder withId(UUID id);

  /**
   * Sets the name of this stub mapping.
   *
   * @param name the name
   * @return this builder
   */
  MappingBuilder withName(String name);

  /**
   * Sets this stub mapping as persistent.
   *
   * @return this builder
   */
  MappingBuilder persistent();

  /**
   * Sets whether this stub mapping is persistent.
   *
   * @param persistent true if the stub mapping should be persistent, false otherwise
   * @return this builder
   */
  MappingBuilder persistent(boolean persistent);

  /**
   * Sets the basic authentication credentials to match on.
   *
   * @param username the username
   * @param password the password
   * @return this builder
   */
  MappingBuilder withBasicAuth(String username, String password);

  /**
   * Sets a cookie to match on.
   *
   * @param name the cookie name
   * @param cookieValuePattern the cookie value pattern
   * @return this builder
   */
  MappingBuilder withCookie(String name, StringValuePattern cookieValuePattern);

  /**
   * Adds a post-serve action.
   *
   * @param extensionName the name of the extension
   * @param parameters the parameters for the extension
   * @param <P> the type of the parameters
   * @return this builder
   */
  <P> MappingBuilder withPostServeAction(String extensionName, P parameters);

  /**
   * Adds a serve event listener.
   *
   * @param requestPhase the request phase
   * @param extensionName the name of the extension
   * @param parameters the parameters for the extension
   * @param <P> the type of the parameters
   * @return this builder
   */
  default <P> MappingBuilder withServeEventListener(
      ServeEventListener.RequestPhase requestPhase, String extensionName, P parameters) {
    return withServeEventListener(Set.of(requestPhase), extensionName, parameters);
  }

  /**
   * Adds a serve event listener.
   *
   * @param requestPhases the request phases
   * @param extensionName the name of the extension
   * @param parameters the parameters for the extension
   * @param <P> the type of the parameters
   * @return this builder
   */
  <P> MappingBuilder withServeEventListener(
      Set<ServeEventListener.RequestPhase> requestPhases, String extensionName, P parameters);

  /**
   * Adds a serve event listener.
   *
   * @param extensionName the name of the extension
   * @param parameters the parameters for the extension
   * @param <P> the type of the parameters
   * @return this builder
   */
  <P> MappingBuilder withServeEventListener(String extensionName, P parameters);

  /**
   * Sets the metadata for this stub mapping.
   *
   * @param metadata the metadata
   * @return this builder
   */
  MappingBuilder withMetadata(Map<String, ?> metadata);

  /**
   * Sets the metadata for this stub mapping.
   *
   * @param metadata the metadata
   * @return this builder
   */
  MappingBuilder withMetadata(Metadata metadata);

  /**
   * Sets the metadata for this stub mapping.
   *
   * @param metadata the metadata builder
   * @return this builder
   */
  MappingBuilder withMetadata(Metadata.Builder metadata);

  /**
   * Adds a custom request matcher.
   *
   * @param requestMatcher the request matcher
   * @return this builder
   */
  MappingBuilder andMatching(ValueMatcher<Request> requestMatcher);

  /**
   * Adds a custom request matcher by name.
   *
   * @param customRequestMatcherName the name of the custom request matcher
   * @return this builder
   */
  MappingBuilder andMatching(String customRequestMatcherName);

  /**
   * Adds a custom request matcher by name.
   *
   * @param customRequestMatcherName the name of the custom request matcher
   * @param parameters the parameters for the custom request matcher
   * @return this builder
   */
  MappingBuilder andMatching(String customRequestMatcherName, Parameters parameters);

  /**
   * Adds a custom request matcher.
   *
   * @param matcherDefinition the custom matcher definition
   * @return this builder
   */
  MappingBuilder andMatching(CustomMatcherDefinition matcherDefinition);

  /**
   * Sets the response definition for this stub mapping.
   *
   * @param responseDefBuilder the response definition builder
   * @return this builder
   */
  MappingBuilder willReturn(ResponseDefinitionBuilder responseDefBuilder);

  /**
   * Builds the stub mapping.
   *
   * @return the stub mapping
   */
  StubMapping build();
}
