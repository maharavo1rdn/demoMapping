package adapter;

public interface InboundAdapter {
    Object processInbound(String providerName, String rawJsonPayload);
}