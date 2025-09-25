/*
 * Copyright (C) 2020-2025 Thomas Akehurst
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
package com.github.tomakehurst.wiremock.http.ssl;

import static com.github.tomakehurst.wiremock.common.ArrayFunctions.prepend;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.net.ssl.SNIHostName;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

/**
 * A Certificate Authority (CA) that can be used to generate a root certificate and sign new
 * certificates.
 *
 * <p>This implementation uses the Bouncy Castle library to generate certificates.
 */
public class CertificateAuthority {

  static {
    // Add Bouncy Castle as a security provider
    Security.addProvider(new BouncyCastleProvider());
  }

  private final X509Certificate[] certificateChain;
  private final PrivateKey key;

  /**
   * Creates a new CertificateAuthority.
   *
   * @param certificateChain the certificate chain, with the CA's certificate at the top
   * @param key the private key of the CA
   */
  public CertificateAuthority(X509Certificate[] certificateChain, PrivateKey key) {
    this.certificateChain = requireNonNull(certificateChain);
    if (certificateChain.length == 0) {
      throw new IllegalArgumentException("Chain must have entries");
    }
    this.key = requireNonNull(key);
  }

  /**
   * Generates a new self-signed Certificate Authority.
   *
   * @return a new CertificateAuthority
   * @throws CertificateGenerationUnsupportedException if the runtime does not support generating
   *     certificates
   */
  public static CertificateAuthority generateCertificateAuthority()
      throws CertificateGenerationUnsupportedException {
    try {
      KeyPair pair = generateKeyPair("RSA");
      X509Certificate certificate =
          selfSign(
              pair,
              "SHA256WithRSA",
              "CN=WireMock Local Self Signed Root Certificate",
              Period.ofYears(10));
      return new CertificateAuthority(new X509Certificate[] {certificate}, pair.getPrivate());
    } catch (Exception e) {
      throw new CertificateGenerationUnsupportedException(
          "Your runtime does not support generating certificates at runtime", e);
    }
  }

  private static X509Certificate selfSign(
      KeyPair keyPair, String signatureAlgorithm, String subject, Period validity)
      throws OperatorCreationException,
          CertificateException,
          IOException,
          NoSuchAlgorithmException {
    X500Name subjectDN = new X500Name(subject);
    ZonedDateTime start = ZonedDateTime.now().minus(Period.ofDays(1));
    ZonedDateTime end = start.plus(validity);
    BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());

    X509v3CertificateBuilder builder =
        new JcaX509v3CertificateBuilder(
            subjectDN,
            serial,
            Date.from(start.toInstant()),
            Date.from(end.toInstant()),
            subjectDN,
            keyPair.getPublic());

    JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
    builder.addExtension(
        Extension.subjectKeyIdentifier,
        false,
        extUtils.createSubjectKeyIdentifier(keyPair.getPublic()));
    builder.addExtension(
        Extension.authorityKeyIdentifier,
        false,
        extUtils.createAuthorityKeyIdentifier(keyPair.getPublic()));
    builder.addExtension(
        Extension.basicConstraints, true, new BasicConstraints(true)); // Mark as CA
    builder.addExtension(
        Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign));

    ContentSigner signer =
        new JcaContentSignerBuilder(signatureAlgorithm).build(keyPair.getPrivate());
    return new JcaX509CertificateConverter().getCertificate(builder.build(signer));
  }

  /**
   * Returns the certificate chain for this CA.
   *
   * @return the certificate chain
   */
  public X509Certificate[] certificateChain() {
    return certificateChain;
  }

  /**
   * Returns the private key for this CA.
   *
   * @return the private key
   */
  public PrivateKey key() {
    return key;
  }

  CertChainAndKey generateCertificate(String keyType, SNIHostName hostName)
      throws CertificateGenerationUnsupportedException {
    try {
      KeyPair pair = generateKeyPair(keyType);
      String sigAlg = "SHA256With" + keyType;
      X509Certificate certificate =
          sign(pair, sigAlg, "CN=" + hostName.getAsciiName(), Period.ofYears(1), hostName);

      X509Certificate[] fullChain = prepend(certificate, certificateChain);
      return new CertChainAndKey(fullChain, pair.getPrivate());
    } catch (Exception e) {
      throw new CertificateGenerationUnsupportedException(
          "Your runtime does not support generating certificates at runtime", e);
    }
  }

  private X509Certificate sign(
      KeyPair keyPair,
      String signatureAlgorithm,
      String subject,
      Period validity,
      SNIHostName hostName)
      throws OperatorCreationException,
          CertificateException,
          IOException,
          NoSuchAlgorithmException {
    X509Certificate issuerCertificate = certificateChain[0];
    X500Name issuerDN = new X500Name(issuerCertificate.getSubjectX500Principal().getName());
    X500Name subjectDN = new X500Name(subject);
    ZonedDateTime start = ZonedDateTime.now().minus(Period.ofDays(1));
    ZonedDateTime end = start.plus(validity);
    BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());

    X509v3CertificateBuilder builder =
        new JcaX509v3CertificateBuilder(
            issuerDN,
            serial,
            Date.from(start.toInstant()),
            Date.from(end.toInstant()),
            subjectDN,
            keyPair.getPublic());

    JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
    builder.addExtension(
        Extension.subjectKeyIdentifier,
        false,
        extUtils.createSubjectKeyIdentifier(keyPair.getPublic()));
    builder.addExtension(
        Extension.authorityKeyIdentifier,
        false,
        extUtils.createAuthorityKeyIdentifier(issuerCertificate.getPublicKey()));
    builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(false)); // Not a CA

    GeneralNames subjectAlternativeNames =
        new GeneralNames(new GeneralName(GeneralName.dNSName, hostName.getAsciiName()));
    builder.addExtension(Extension.subjectAlternativeName, false, subjectAlternativeNames);

    ContentSigner signer = new JcaContentSignerBuilder(signatureAlgorithm).build(key);
    return new JcaX509CertificateConverter().getCertificate(builder.build(signer));
  }

  private static KeyPair generateKeyPair(String keyType) throws NoSuchAlgorithmException {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keyType);
    keyGen.initialize(2048, new SecureRandom());
    return keyGen.generateKeyPair();
  }
}
