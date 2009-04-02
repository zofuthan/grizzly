/*
 * 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2007-2008 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 *
 */

package com.sun.grizzly;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * SSL configuration
 *
 * @author Alexey Stashok
 * @author Hubert Iwaniuk
 */
public class SSLConfig {
    public static final String TRUST_STORE_FILE = "javax.net.ssl.trustStore";
    
    public static final String KEY_STORE_FILE = "javax.net.ssl.keyStore";
    
    public static final String TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";
    
    public static final String KEY_STORE_PASSWORD = "javax.net.ssl.keyStorePassword";

    public static final String TRUST_STORE_TYPE = "javax.net.ssl.trustStoreType";
    
    public static final String KEY_STORE_TYPE = "javax.net.ssl.keyStoreType";
    public static final String DEFAULT_STORE_PASSWORD = "changeit";
    public static final String DEFAULT_TRUSTSTORE_NAME = "truststore.jks";
    public static final String DEFAULT_KEYSTORE_NAME = "keystore.jks";

    /**
     * Default Logger.
     */
    private static Logger logger = Logger.getLogger("grizzly");
    
    /**
     * Default SSL configuration
     */
    public static SSLConfig DEFAULT_CONFIG = new SSLConfig();

    private String trustStoreType;
    private String keyStoreType;

    private char[] trustStorePass;

    private char[] keyStorePass;

    private String trustStoreFile;
    private String keyStoreFile;

    private String trustStoreAlgorithm;
    private String keyStoreAlgorithm;

    private String securityProtocol = "TLS";

    private boolean clientMode = false;

    private boolean needClientAuth = false;

    private boolean wantClientAuth = false;
    private static final String DEFAULT_STORE_TYPE = "JKS";
    private static final String DEFAULT_STORE_ALGORITH = "SunX509";

    public SSLConfig() {
        this(true);
    }
    
    public SSLConfig(boolean readSystemProperties) {
        if (readSystemProperties) {
            retrieve(System.getProperties());
        }
    }

    public String getTrustStoreType() {
        return trustStoreType;
    }
    
    public void setTrustStoreType(String trustStoreType) {
        this.trustStoreType = trustStoreType;
    }
    
    public String getKeyStoreType() {
        return keyStoreType;
    }
    
    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }
    
    public String getTrustStorePass() {
        return new String(trustStorePass);
    }
    
    public void setTrustStorePass(String trustStorePass) {
        this.trustStorePass = trustStorePass.toCharArray();
    }
    
    public String getKeyStorePass() {
        return new String(keyStorePass);
    }
    
    public void setKeyStorePass(String keyStorePass) {
        this.keyStorePass = keyStorePass.toCharArray();
    }
    
    public String getTrustStoreFile() {
        return trustStoreFile;
    }

    /**
     * Sets trust store file name, also makes sute that if other trust store
     * configutation parameters are not set to set them to default values.
     *
     * @param trustStoreFile File name of trust store.
     */
    public void setTrustStoreFile(String trustStoreFile) {
        this.trustStoreFile = trustStoreFile;
        if (trustStoreType == null) {
            trustStoreType = DEFAULT_STORE_TYPE;
        }
        if (trustStoreAlgorithm == null) {
            trustStoreAlgorithm = DEFAULT_STORE_ALGORITH;
        }
        if (trustStorePass == null || trustStorePass.length == 0) {
            trustStorePass = DEFAULT_STORE_PASSWORD.toCharArray();
        }
    }
    
    public String getKeyStoreFile() {
        return keyStoreFile;
    }

    /**
     * Sets key store file name, also makes sure that if other key store
     * configuration parameters are not set to set them to default values.
     *
     * @param keyStoreFile File name of key store.
     */
    public void setKeyStoreFile(String keyStoreFile) {
        this.keyStoreFile = keyStoreFile;
        if (keyStoreType == null) {
            keyStoreType = DEFAULT_STORE_TYPE;
        }
        if (keyStoreAlgorithm == null) {
            keyStoreAlgorithm = DEFAULT_STORE_ALGORITH;
        }
        if (keyStorePass == null || keyStorePass.length == 0) {
            keyStorePass = DEFAULT_STORE_PASSWORD.toCharArray();
        }
    }
    
    public String getTrustStoreAlgorithm() {
        return trustStoreAlgorithm;
    }
    
    public void setTrustStoreAlgorithm(String trustStoreAlgorithm) {
        this.trustStoreAlgorithm = trustStoreAlgorithm;
    }
    
    public String getKeyStoreAlgorithm() {
        return keyStoreAlgorithm;
    }
    
    public void setKeyStoreAlgorithm(String keyStoreAlgorithm) {
        this.keyStoreAlgorithm = keyStoreAlgorithm;
    }
    
    public String getSecurityProtocol() {
        return securityProtocol;
    }
    
    public void setSecurityProtocol(String securityProtocol) {
        this.securityProtocol = securityProtocol;
    }
    
    public boolean isNeedClientAuth() {
        return needClientAuth;
    }
    
    public void setNeedClientAuth(boolean needClientAuth) {
        this.needClientAuth = needClientAuth;
    }
    
    public boolean isWantClientAuth() {
        return wantClientAuth;
    }
    
    public void setWantClientAuth(boolean wantClientAuth) {
        this.wantClientAuth = wantClientAuth;
    }
    
    public boolean isClientMode() {
        return clientMode;
    }
    
    public void setClientMode(boolean clientMode) {
        this.clientMode = clientMode;
    }

    /**
     * Validates {@link SSLConfig} configuration.
     *
     * @return <code>true</code> iff configuration is valid, else <code>false</code>.
     */
    public boolean validateConfiguration() {
        boolean valid = false;
        try {
            TrustManagerFactory trustManagerFactory;
            KeyManagerFactory keyManagerFactory;

            if (keyStoreFile != null) {
                try {
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                    keyStore.load(new FileInputStream(keyStoreFile),
                            keyStorePass);

                    keyManagerFactory =
                            KeyManagerFactory.getInstance(keyStoreAlgorithm);
                    keyManagerFactory.init(keyStore, keyStorePass);
                    valid = true;
                } catch (KeyStoreException e) {
                    logger.log(Level.FINE, "Error initializing key store", e);
                } catch (CertificateException e) {
                    logger.log(Level.FINE, "Key store certificate exception.", e);
                } catch (UnrecoverableKeyException e) {
                    logger.log(Level.FINE, "Key store unrecoverable exception.", e);
                } catch (FileNotFoundException e) {
                    logger.log(Level.FINE, "Can't find key store file: " + keyStoreFile, e);
                } catch (IOException e) {
                    logger.log(Level.FINE, "Error loading key store from file: " + keyStoreFile, e);
                }
            }

            if (trustStoreFile != null) {
                try {
                    KeyStore trustStore = KeyStore.getInstance(trustStoreType);
                    trustStore.load(new FileInputStream(trustStoreFile),
                            trustStorePass);

                    trustManagerFactory =
                            TrustManagerFactory.getInstance(trustStoreAlgorithm);
                    trustManagerFactory.init(trustStore);
                    valid = true;
                } catch (KeyStoreException e) {
                    logger.log(Level.FINE, "Error initializing trust store", e);
                    valid = false;
                } catch (CertificateException e) {
                    logger.log(Level.FINE, "Trust store certificate exception.", e);
                    valid = false;
                } catch (FileNotFoundException e) {
                    logger.log(Level.FINE, "Can't find trust store file: " + trustStoreFile, e);
                } catch (IOException e) {
                    logger.log(Level.FINE, "Error loading trust store from file: " + trustStoreFile, e);
                }
            }
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.FINE, "Error initializing algorithm.", e);
            valid = false;
        }
        return valid;
    }

    public SSLContext createSSLContext() {
        SSLContext sslContext = null;
        
        try {
            TrustManagerFactory trustManagerFactory = null;
            KeyManagerFactory keyManagerFactory = null;
            
            if (trustStoreFile != null) {
                try {
                    KeyStore trustStore = KeyStore.getInstance(trustStoreType);
                    trustStore.load(new FileInputStream(trustStoreFile),
                            trustStorePass);
                    
                    trustManagerFactory =
                            TrustManagerFactory.getInstance(trustStoreAlgorithm);
                    trustManagerFactory.init(trustStore);
                } catch (KeyStoreException e) {
                    logger.log(Level.FINE, "Error initializing trust store", e);
                } catch (CertificateException e) {
                    logger.log(Level.FINE, "Trust store certificate exception.", e);
                } catch (FileNotFoundException e) {
                    logger.log(Level.FINE, "Can't find trust store file: " + trustStoreFile, e);
                } catch (IOException e) {
                    logger.log(Level.FINE, "Error loading trust store from file: " + trustStoreFile, e);
                }
            }
            
            if (keyStoreFile != null) {
                try {
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                    keyStore.load(new FileInputStream(keyStoreFile),
                            keyStorePass);
                    
                    keyManagerFactory =
                            KeyManagerFactory.getInstance(keyStoreAlgorithm);
                    keyManagerFactory.init(keyStore, keyStorePass);
                } catch (KeyStoreException e) {
                    logger.log(Level.FINE, "Error initializing key store", e);
                } catch (CertificateException e) {
                    logger.log(Level.FINE, "Key store certificate exception.", e);
                } catch (UnrecoverableKeyException e) {
                    logger.log(Level.FINE, "Key store unrecoverable exception.", e);
                } catch (FileNotFoundException e) {
                    logger.log(Level.FINE, "Can't find key store file: " + keyStoreFile, e);
                } catch (IOException e) {
                    logger.log(Level.FINE, "Error loading key store from file: " + keyStoreFile, e);
                }
            }
            
            sslContext = SSLContext.getInstance(securityProtocol);
            sslContext.init(keyManagerFactory != null ? keyManagerFactory.getKeyManagers() : null,
                    trustManagerFactory != null ? trustManagerFactory.getTrustManagers() : null,
                    null);
        } catch (KeyManagementException e) {
            logger.log(Level.FINE, "Key management error.", e);
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.FINE, "Error initializing algorithm.", e);
        }
        
        return sslContext;
    }
    
    public void retrieve(Properties props) {
        trustStoreType = props.getProperty(TRUST_STORE_TYPE, DEFAULT_STORE_TYPE);
        keyStoreType = props.getProperty(KEY_STORE_TYPE, DEFAULT_STORE_TYPE);
    
        trustStorePass = 
                props.getProperty(TRUST_STORE_PASSWORD, DEFAULT_STORE_PASSWORD).toCharArray();
    
        keyStorePass = 
                props.getProperty(KEY_STORE_PASSWORD, DEFAULT_STORE_PASSWORD).toCharArray();

        trustStoreFile = props.getProperty(TRUST_STORE_FILE, DEFAULT_TRUSTSTORE_NAME);
        keyStoreFile = props.getProperty(KEY_STORE_FILE, DEFAULT_KEYSTORE_NAME);
    
        trustStoreAlgorithm = DEFAULT_STORE_ALGORITH;
        keyStoreAlgorithm = DEFAULT_STORE_ALGORITH;
    
        securityProtocol = "TLS";
    }
    
    public void publish(Properties props) {
        props.setProperty(TRUST_STORE_FILE, trustStoreFile);
        props.setProperty(KEY_STORE_FILE, keyStoreFile);
        
        props.setProperty(TRUST_STORE_PASSWORD, new String(trustStorePass));
        props.setProperty(KEY_STORE_PASSWORD, new String(keyStorePass));

        props.setProperty(TRUST_STORE_TYPE, trustStoreType);
        props.setProperty(KEY_STORE_TYPE, keyStoreType);
    }
}
