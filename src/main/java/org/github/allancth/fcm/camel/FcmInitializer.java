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
package org.github.allancth.fcm.camel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;

public class FcmInitializer extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(FcmInitializer.class);

    @Override
    public void init() throws ServletException {
        super.init();

        LOG.debug("Add provider for FcmPacketExtension");

        ProviderManager.addExtensionProvider(FcmPacketExtension.ELEMENT_NAME, FcmPacketExtension.NAMESPACE, new PacketExtensionProvider() {

            @Override
            public PacketExtension parseExtension(final XmlPullParser parser) throws Exception {
                return new FcmPacketExtension(parser.nextText());
            }
        });
    }

    @Override
    public void destroy() {
        LOG.debug("Remove provider for FcmPacketExtension");

        ProviderManager.removeExtensionProvider(FcmPacketExtension.ELEMENT_NAME, FcmPacketExtension.NAMESPACE);

        super.destroy();
    }
}
