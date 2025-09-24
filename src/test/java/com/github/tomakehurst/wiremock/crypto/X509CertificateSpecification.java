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
package com.github.tomakehurst.wiremock.crypto;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class X509CertificateSpecification implements CertificateSpecification {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  private final X509CertificateVersion version;
  private final X500Name subject;
  private final X500Name issuer;
  private final Date notBefore;
  private final Date notAfter;

  public X509CertificateSpecification(
      X509CertificateVersion version, String subject, String issuer, Date notBefore, Date notAfter)
      throws IOException {
    this.version = version;
    this.subject = new X500Name(subject);
    this.issuer = new X500Name(issuer);
    this.notBefore = notBefore;
    this.notAfter = notAfter;
  }

  @Override
  public X509Certificate certificateFor(KeyPair keyPair)
      throws CertificateException, InvalidKeyException, SignatureException {
    try {
      BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());

      X509v3CertificateBuilder certBuilder =
          new JcaX509v3CertificateBuilder(
              issuer, serial, notBefore, notAfter, subject, keyPair.getPublic());

      ContentSigner signer =
          new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate());

      return new JcaX509CertificateConverter()
          .setProvider("BC")
          .getCertificate(certBuilder.build(signer));
    } catch (OperatorCreationException | GeneralSecurityException e) {
      return throwUnchecked(e, X509Certificate.class);
    }
  }
}
