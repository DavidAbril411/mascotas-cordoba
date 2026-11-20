package ar.edu.ubp.das.veterinariaws.config;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.headers.Header;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Element;
import org.apache.cxf.interceptor.Fault;

public class ApiKeyInterceptor extends AbstractSoapInterceptor {

    private final String configuredApiKey;

    public ApiKeyInterceptor(String configuredApiKey) {
        super(Phase.PRE_PROTOCOL);
        this.configuredApiKey = configuredApiKey;
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        String apiKey = readApiKeyFromHeader(message);
        if (!configuredApiKey.equals(apiKey)) {
            throw new SoapFault(
                    "Invalid API Key",
                    SoapFault.FAULT_CODE_CLIENT
            );
        }
    }

    private String readApiKeyFromHeader(SoapMessage message) {
        for (Header header : message.getHeaders()) {
            if ("apiKey".equals(header.getName().getLocalPart())) {
                Object object = header.getObject();
                if (object instanceof Element element) {
                    return element.getTextContent().trim();
                }
            }
        }
        return null;
    }

}