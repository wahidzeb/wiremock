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

import com.github.tomakehurst.wiremock.common.*;
import com.github.tomakehurst.wiremock.common.filemaker.FilenameMaker;
import com.github.tomakehurst.wiremock.extension.ExtensionDeclarations;
import com.github.tomakehurst.wiremock.extension.Extensions;
import com.github.tomakehurst.wiremock.http.CaseInsensitiveKey;
import com.github.tomakehurst.wiremock.http.HttpServerFactory;
import com.github.tomakehurst.wiremock.http.ThreadPoolFactory;
import com.github.tomakehurst.wiremock.http.client.HttpClientFactory;
import com.github.tomakehurst.wiremock.http.trafficlistener.WiremockNetworkTrafficListener;
import com.github.tomakehurst.wiremock.security.Authenticator;
import com.github.tomakehurst.wiremock.standalone.MappingsLoader;
import com.github.tomakehurst.wiremock.store.Stores;
import com.github.tomakehurst.wiremock.verification.notmatched.NotMatchedRenderer;
import com.github.tomakehurst.wiremock.verification.notmatched.PlainTextStubNotMatchedRenderer;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * The configuration options for a WireMock server.
 *
 * @see WireMockConfiguration
 */
public interface Options {

  /** The policy for chunked encoding. */
  enum ChunkedEncodingPolicy {
    /** Always use chunked encoding. */
    ALWAYS,
    /** Never use chunked encoding. */
    NEVER,
    /** Use chunked encoding only for body files. */
    BODY_FILE
  }

  int DEFAULT_PORT = 8080;
  int DYNAMIC_PORT = 0;
  int DEFAULT_TIMEOUT = 300_000;
  int DEFAULT_CONTAINER_THREADS = 25;
  String DEFAULT_BIND_ADDRESS = "0.0.0.0";
  int DEFAULT_MAX_HTTP_CONNECTIONS = 1000;
  int DEFAULT_WEBHOOK_THREADPOOL_SIZE = 10;
  boolean DEFAULT_DISABLE_CONNECTION_REUSE = true;
  Long DEFAULT_MAX_TEMPLATE_CACHE_ENTRIES = 1000L;

  /**
   * Gets the port number for the WireMock server.
   *
   * @return the port number
   */
  int portNumber();

  /**
   * Whether HTTP is disabled.
   *
   * @return true if HTTP is disabled, false otherwise
   */
  boolean getHttpDisabled();

  /**
   * Whether HTTP/2 over plain text is disabled.
   *
   * @return true if HTTP/2 over plain text is disabled, false otherwise
   */
  boolean getHttp2PlainDisabled();

  /**
   * Whether HTTP/2 over TLS is disabled.
   *
   * @return true if HTTP/2 over TLS is disabled, false otherwise
   */
  boolean getHttp2TlsDisabled();

  /**
   * Gets the HTTPS settings.
   *
   * @return the HTTPS settings
   */
  HttpsSettings httpsSettings();

  /**
   * Gets the Jetty settings.
   *
   * @return the Jetty settings
   */
  JettySettings jettySettings();

  /**
   * Gets the number of container threads.
   *
   * @return the number of container threads
   */
  int containerThreads();

  /**
   * Whether browser proxying is enabled.
   *
   * @return true if browser proxying is enabled, false otherwise
   * @deprecated use {@link BrowserProxySettings#enabled()}
   */
  @Deprecated
  boolean browserProxyingEnabled();

  /**
   * Gets the browser proxy settings.
   *
   * @return the browser proxy settings
   */
  BrowserProxySettings browserProxySettings();

  /**
   * Gets the proxy settings.
   *
   * @return the proxy settings
   */
  ProxySettings proxyVia();

  /**
   * Gets the stores.
   *
   * @return the stores
   */
  Stores getStores();

  /**
   * Gets the files root file source.
   *
   * @return the files root file source
   */
  FileSource filesRoot();

  /**
   * Gets the mappings loader.
   *
   * @return the mappings loader
   */
  MappingsLoader mappingsLoader();

  /**
   * Gets the mappings saver.
   *
   * @return the mappings saver
   */
  MappingsSaver mappingsSaver();

  /**
   * Gets the notifier.
   *
   * @return the notifier
   */
  Notifier notifier();

  /**
   * Whether the request journal is disabled.
   *
   * @return true if the request journal is disabled, false otherwise
   */
  boolean requestJournalDisabled();

  /**
   * Gets the maximum number of entries in the request journal.
   *
   * @return the maximum number of entries, or empty if unlimited
   */
  Optional<Integer> maxRequestJournalEntries();

  /**
   * Gets the bind address.
   *
   * @return the bind address
   */
  String bindAddress();

  /**
   * Gets the filename maker.
   *
   * @return the filename maker
   */
  FilenameMaker getFilenameMaker();

  /**
   * Gets the list of headers that should be recorded for matching.
   *
   * @return the list of headers
   */
  List<CaseInsensitiveKey> matchingHeaders();

  /**
   * Whether the Host header should be preserved.
   *
   * @return true if the Host header should be preserved, false otherwise
   */
  boolean shouldPreserveHostHeader();

  /**
   * Whether the User-Agent header should be preserved in proxy responses.
   *
   * @return true if the User-Agent header should be preserved, false otherwise
   */
  boolean shouldPreserveUserAgentProxyHeader();

  /**
   * Gets the value of the Host header to be used in proxy requests.
   *
   * @return the value of the Host header
   */
  String proxyHostHeader();

  /**
   * Gets the HTTP server factory.
   *
   * @return the HTTP server factory
   */
  HttpServerFactory httpServerFactory();

  /**
   * Whether the default HTTP server factory is being used.
   *
   * @return true if the default HTTP server factory is being used, false otherwise
   */
  boolean hasDefaultHttpServerFactory();

  /**
   * Gets the HTTP client factory.
   *
   * @return the HTTP client factory
   */
  HttpClientFactory httpClientFactory();

  /**
   * Gets the thread pool factory.
   *
   * @return the thread pool factory
   */
  ThreadPoolFactory threadPoolFactory();

  /**
   * Gets the declared extensions.
   *
   * @return the declared extensions
   */
  ExtensionDeclarations getDeclaredExtensions();

  /**
   * Whether extension scanning is enabled.
   *
   * @return true if extension scanning is enabled, false otherwise
   */
  boolean isExtensionScanningEnabled();

  /**
   * Gets the network traffic listener.
   *
   * @return the network traffic listener
   */
  WiremockNetworkTrafficListener networkTrafficListener();

  /**
   * Gets the admin authenticator.
   *
   * @return the admin authenticator
   */
  Authenticator getAdminAuthenticator();

  /**
   * Whether HTTPS is required for the admin API.
   *
   * @return true if HTTPS is required for the admin API, false otherwise
   */
  boolean getHttpsRequiredForAdminApi();

  /**
   * Gets the factory for the not-matched renderer.
   *
   * @return the factory for the not-matched renderer
   */
  default Function<Extensions, NotMatchedRenderer> getNotMatchedRendererFactory() {
    return PlainTextStubNotMatchedRenderer::new;
  }

  /**
   * Gets the asynchronous response settings.
   *
   * @return the asynchronous response settings
   */
  AsynchronousResponseSettings getAsynchronousResponseSettings();

  /**
   * Gets the chunked encoding policy.
   *
   * @return the chunked encoding policy
   */
  ChunkedEncodingPolicy getChunkedEncodingPolicy();

  /**
   * Whether GZIP is disabled.
   *
   * @return true if GZIP is disabled, false otherwise
   */
  boolean getGzipDisabled();

  /**
   * Whether stub request logging is disabled.
   *
   * @return true if stub request logging is disabled, false otherwise
   */
  boolean getStubRequestLoggingDisabled();

  /**
   * Whether stub CORS is enabled.
   *
   * @return true if stub CORS is enabled, false otherwise
   */
  boolean getStubCorsEnabled();

  /**
   * Gets the timeout in milliseconds.
   *
   * @return the timeout in milliseconds
   */
  long timeout();

  /**
   * Whether to disable the optimization of XML factories loading.
   *
   * @return true if the optimization is disabled, false otherwise
   */
  boolean getDisableOptimizeXmlFactoriesLoading();

  /**
   * Whether to disable strict HTTP headers.
   *
   * @return true if strict HTTP headers are disabled, false otherwise
   */
  boolean getDisableStrictHttpHeaders();

  /**
   * Gets the data truncation settings.
   *
   * @return the data truncation settings
   */
  DataTruncationSettings getDataTruncationSettings();

  /**
   * Gets the proxy target rules.
   *
   * @return the proxy target rules
   */
  NetworkAddressRules getProxyTargetRules();

  /**
   * Gets the proxy timeout in milliseconds.
   *
   * @return the proxy timeout in milliseconds
   */
  int proxyTimeout();

  /**
   * Gets the maximum number of HTTP client connections.
   *
   * @return the maximum number of HTTP client connections
   */
  int getMaxHttpClientConnections();

  /**
   * Whether response templating is enabled.
   *
   * @return true if response templating is enabled, false otherwise
   */
  boolean getResponseTemplatingEnabled();

  /**
   * Whether global response templating is enabled.
   *
   * @return true if global response templating is enabled, false otherwise
   */
  boolean getResponseTemplatingGlobal();

  /**
   * Gets the maximum number of template cache entries.
   *
   * @return the maximum number of template cache entries
   */
  Long getMaxTemplateCacheEntries();

  /**
   * Gets the set of permitted system keys for template helpers.
   *
   * @return the set of permitted system keys
   */
  Set<String> getTemplatePermittedSystemKeys();

  /**
   * Whether template escaping is disabled.
   *
   * @return true if template escaping is disabled, false otherwise
   */
  boolean getTemplateEscapingDisabled();

  /**
   * Gets the set of supported proxy encodings.
   *
   * @return the set of supported proxy encodings
   */
  Set<String> getSupportedProxyEncodings();

  /**
   * Whether connection reuse is disabled.
   *
   * @return true if connection reuse is disabled, false otherwise
   */
  boolean getDisableConnectionReuse();

  /**
   * Gets the webhook thread pool size.
   *
   * @return the webhook thread pool size
   */
  int getWebhookThreadPoolSize();
}
