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
