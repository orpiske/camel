/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.pqc.crypto.kem;

import java.security.*;

import javax.crypto.KeyGenerator;

import org.apache.camel.component.pqc.PQCKeyEncapsulationAlgorithms;
import org.bouncycastle.jcajce.spec.MLKEMParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;

public class PQCDefaultMLKEMMaterial {

    public static final KeyPair keyPair;
    public static final KeyGenerator keyGenerator;
    public static final KeyPairGenerator generator;

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        if (Security.getProvider(BouncyCastlePQCProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastlePQCProvider());
        }
        try {
            generator = prepareKeyPair();
            keyPair = generator.generateKeyPair();
            keyGenerator = prepareKeyGenerator();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static KeyPairGenerator prepareKeyPair()
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(PQCKeyEncapsulationAlgorithms.MLKEM.getAlgorithm(),
                PQCKeyEncapsulationAlgorithms.MLKEM.getBcProvider());
        kpg.initialize(MLKEMParameterSpec.ml_kem_512, new SecureRandom());
        return kpg;
    }

    protected static KeyGenerator prepareKeyGenerator() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator kg = KeyGenerator.getInstance(PQCKeyEncapsulationAlgorithms.MLKEM.getAlgorithm(),
                PQCKeyEncapsulationAlgorithms.MLKEM.getBcProvider());
        return kg;
    }
}
