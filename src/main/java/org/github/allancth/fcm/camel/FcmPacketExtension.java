package org.github.allancth.fcm.camel;

import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class FcmPacketExtension extends DefaultPacketExtension {

    public static String ELEMENT_NAME = "gcm";

    public static String NAMESPACE = "google:mobile:data";

    private String json;

    public FcmPacketExtension(String json) {
        super(ELEMENT_NAME, NAMESPACE);
        this.json = json;
    }

    public String getJson() {
        return json;
    }

    @Override
    public String toXML() {
        return String.format("<%s xmlns=\"%s\">%s</%s>", ELEMENT_NAME, NAMESPACE, json, ELEMENT_NAME);
    }

    public Packet toPacket() {
        return new Message() {

            @Override
            public XmlStringBuilder toXML() {

                final XmlStringBuilder xmlStringBuilder = new XmlStringBuilder().append("<message");
                if (getXmlns() != null) {
                    xmlStringBuilder.append(" xmlns=\"").append(getXmlns()).append("\"");
                }

                if (getLanguage() != null) {
                    xmlStringBuilder.append(" xml:lang=\"").append(getLanguage()).append("\"");
                }

                if (getPacketID() != null) {
                    xmlStringBuilder.append(" id=\"").append(getPacketID()).append("\"");
                }

                if (getTo() != null) {
                    xmlStringBuilder.append(" to=\"").append(StringUtils.escapeForXML(getTo())).append("\"");
                }

                if (getFrom() != null) {
                    xmlStringBuilder.append(" from=\"").append(StringUtils.escapeForXML(getFrom())).append("\"");
                }

                xmlStringBuilder.append(">").append(FcmPacketExtension.this.toXML()).append("</message>");

                return xmlStringBuilder;
            }
        };
    }
}
