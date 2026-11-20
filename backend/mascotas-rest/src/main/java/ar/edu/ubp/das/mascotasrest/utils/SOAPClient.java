package ar.edu.ubp.das.mascotasrest.utils;

import com.google.gson.Gson;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.*;
import jakarta.xml.ws.Dispatch;
import jakarta.xml.ws.Service;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SOAPClient {

    private final String wsdlUrl;
    private final String namespace;
    private final String serviceName;
    private final String portName;
    private final String operationName;
    private final String soapAction;
    private final String apiKey;

    private SOAPClient(SOAPClientBuilder builder) {
        this.wsdlUrl = builder.wsdlUrl;
        this.namespace = builder.namespace;
        this.serviceName = builder.serviceName;
        this.portName = builder.portName;
        this.operationName = builder.operationName;
        this.soapAction = builder.soapAction;
        this.apiKey = builder.apiKey;
    }

    //region Métodos públicos
    public <T> T callServiceForObject(Class<T> clazz, String responseElementName, Map<String, Object> parameters) {
        try {
            SOAPMessage request = createRequest(operation -> addParameters(operation, parameters));
            SOAPMessage response = sendRequest(request);
            return processResponseForObject(response, clazz, responseElementName);
        }
        catch (Exception e) {
            throw new RuntimeException("Error al consumir el servicio SOAP: " + e.getMessage(), e);
        }
    }

    public <T> T callServiceForObject(Class<T> clazz, String responseElementName) {
        return callServiceForObject(clazz, responseElementName, null);
    }

    public <T> T callServiceForObject(Class<T> clazz, String responseElementName, Object parameter) {
        try {
            SOAPMessage request = createRequest(operation -> addObjectParameter(operation, parameter));
            SOAPMessage response = sendRequest(request);
            return processResponseForObject(response, clazz, responseElementName);
        }
        catch (Exception e) {
            throw new RuntimeException("Error al consumir el servicio SOAP: " + e.getMessage(), e);
        }
    }

    public <T> List<T> callServiceForList(Class<T> clazz, String responseElementName, Map<String, Object> parameters) {
        try {
            SOAPMessage request = createRequest(operation -> addParameters(operation, parameters));
            SOAPMessage response = sendRequest(request);
            return processResponseForList(response, clazz, responseElementName);
        }
        catch (Exception e) {
            throw new RuntimeException("Error al consumir el servicio SOAP: " + e.getMessage(), e);
        }
    }

    public <T> List<T> callServiceForList(Class<T> clazz, String responseElementName) {
        return callServiceForList(clazz, responseElementName, null);
    }

    public <T> List<T> callServiceForList(Class<T> clazz, String responseElementName, Object parameter) {
        try {
            SOAPMessage request = createRequest(operation -> addObjectParameter(operation, parameter));
            SOAPMessage response = sendRequest(request);
            return processResponseForList(response, clazz, responseElementName);
        }
        catch (Exception e) {
            throw new RuntimeException("Error al consumir el servicio SOAP: " + e.getMessage(), e);
        }
    }
    //endregion

    //region Creación del request
    private SOAPMessage createRequest(RequestContent content) throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();

        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPBody body = envelope.getBody();

        SOAPElement operation = body.addChildElement(operationName, "tns", namespace);

        content.build(operation);

        addApiKeyHeader(message);

        message.saveChanges();

        return message;
    }

    private void addParameters(SOAPElement operation, Map<String, Object> parameters) throws SOAPException {
        if (parameters == null) {
            return;
        }

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            addParameter(operation, entry.getKey(), entry.getValue());
        }
    }

    private void addParameter(SOAPElement operation, String parameterName, Object parameter) throws SOAPException {
        SOAPElement parameterElement = operation.addChildElement(parameterName);
        parameterElement.addTextNode(String.valueOf(parameter));
    }

    private void addObjectParameter(SOAPElement operation, Object parameter) throws Exception {
        if (parameter == null) {
            return;
        }

        JAXBContext context = JAXBContext.newInstance(parameter.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(parameter, operation);
    }

    private void addApiKeyHeader(SOAPMessage message) throws SOAPException {
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();

        if (header == null) {
            header = envelope.addHeader();
        }

        SOAPElement apiKeyElement = header.addChildElement("apiKey", "sec", "http://das.ubp.edu.ar/security");
        apiKeyElement.addTextNode(apiKey);
    }
    //endregion

    //region Envío
    private SOAPMessage sendRequest(SOAPMessage request) throws Exception {
        URL url = new URL(wsdlUrl);

        QName serviceQName = new QName(namespace, serviceName);
        Service service = Service.create(url, serviceQName);

        QName portQName = new QName(namespace, portName);

        Dispatch<SOAPMessage> dispatch = service.createDispatch(portQName, SOAPMessage.class, Service.Mode.MESSAGE);

        if (soapAction != null && !soapAction.isBlank()) {
            dispatch.getRequestContext().put(Dispatch.SOAPACTION_USE_PROPERTY, true);
            dispatch.getRequestContext().put(Dispatch.SOAPACTION_URI_PROPERTY, soapAction);
        }

        return dispatch.invoke(request);
    }
    //endregion

    //region Procesamiento de respuestas
    private <T> T processResponseForObject(SOAPMessage response, Class<T> clazz, String responseElementName) throws Exception {
        SOAPBody body = response.getSOAPBody();
        checkFault(body);

        SOAPElement responseElement = null;

        Iterator<?> iterator = body.getChildElements();

        while (iterator.hasNext()) {
            Object node = iterator.next();

            if (node instanceof SOAPElement element &&
                    responseElementName.equals(element.getLocalName())) {
                responseElement = element;
                break;
            }
        }

        if (responseElement == null) {
            return null;
        }

        String value = responseElement.getTextContent().trim();
        if (isSimpleType(clazz)) {
            return convertSimpleValue(value, clazz);
        }

        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Iterator<?> children = responseElement.getChildElements();
        while (children.hasNext()) {
            Object child = children.next();

            if (child instanceof SOAPElement) {
                Object result = unmarshaller.unmarshal((SOAPElement) child);

                if (result instanceof jakarta.xml.bind.JAXBElement<?> jaxbElement) {
                    result = jaxbElement.getValue();
                }

                return clazz.cast(result);
            }
        }

        return null;
    }

    private <T> List<T> processResponseForList(SOAPMessage response, Class<T> clazz, String responseElementName) throws Exception {
        SOAPBody body = response.getSOAPBody();

        checkFault(body);

        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        List<T> result = new LinkedList<>();

        Iterator<?> iterator = body.getChildElements();
        while (iterator.hasNext()) {
            Object node = iterator.next();

            if (node instanceof SOAPElement element && responseElementName.equals(element.getLocalName())) {
                Iterator<?> children = element.getChildElements();
                while (children.hasNext()) {
                    Object child = children.next();
                    if (child instanceof SOAPElement) {
                        Object object = unmarshaller.unmarshal((SOAPElement) child);
                        if (object instanceof jakarta.xml.bind.JAXBElement<?> jaxbElement) {
                            object = jaxbElement.getValue();
                        }
                        if (clazz.isInstance(object)) {
                            result.add(clazz.cast(object));
                        }
                    }
                }
            }
        }
        return result;
    }

    private boolean isSimpleType(Class<?> clazz) {
        return clazz == String.class ||
                clazz == Integer.class ||
                clazz == Long.class ||
                clazz == Double.class ||
                clazz == Float.class ||
                clazz == Boolean.class;
    }

    private <T> T convertSimpleValue(String value, Class<T> clazz) {
        try {
            if (clazz == String.class) {
                return clazz.cast(value);
            }
            return clazz.cast(clazz.getMethod("valueOf", String.class).invoke(null, value));
        }
        catch (NoSuchMethodException e) {
            return null;
        }
        catch (Exception e) {
            throw new RuntimeException("No se puede convertir '" + value + "' a " + clazz.getSimpleName(), e);
        }
    }

    private void checkFault(SOAPBody body) throws SOAPException {
        if (body.hasFault()) {
            SOAPFault fault = body.getFault();

            throw new RuntimeException("SOAP Fault: " + fault.getFaultCode() + " - " + fault.getFaultString());
        }
    }
    //endregion

    //region Builder
    public static SOAPClientBuilder fromConfig(String jsonConfig) {
        Gson gson = new Gson();

        SOAPConfig config = gson.fromJson(jsonConfig, SOAPConfig.class);

        return new SOAPClientBuilder()
                .wsdlUrl(config.wsdlUrl)
                .namespace(config.namespace)
                .serviceName(config.serviceName)
                .portName(config.portName)
                .apiKey(config.apiKey);
    }

    public static class SOAPClientBuilder {

        private String wsdlUrl;
        private String namespace;
        private String serviceName;
        private String portName;
        private String operationName;
        private String soapAction;
        private String apiKey;

        private SOAPClientBuilder() {
        }

        public SOAPClientBuilder wsdlUrl(String wsdlUrl) {
            this.wsdlUrl = wsdlUrl;
            return this;
        }

        public SOAPClientBuilder namespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public SOAPClientBuilder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public SOAPClientBuilder portName(String portName) {
            this.portName = portName;
            return this;
        }

        public SOAPClientBuilder operation(String operationName) {
            this.operationName = operationName;
            return this;
        }

        public SOAPClientBuilder soapAction(String soapAction) {
            this.soapAction = soapAction;
            return this;
        }

        public SOAPClientBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public SOAPClient build() {
            return new SOAPClient(this);
        }
        //endregion
    }

    private static class SOAPConfig {

        private String wsdlUrl;
        private String namespace;
        private String serviceName;
        private String portName;
        private String apiKey;

    }

    @FunctionalInterface
    private interface RequestContent {
        void build(SOAPElement operation) throws Exception;
    }
}