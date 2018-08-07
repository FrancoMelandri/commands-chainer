public class ControllerCommand {

    private TypedProperty requestProperties;
    private TypedProperty responseProperties;

    public void setRequestProperties(TypedProperty requestProperties) {
        this.requestProperties = requestProperties;
    }

    public TypedProperty getRequestProperties() {
        return this.requestProperties;
    }

    public void setResponseProperties(TypedProperty responseProperties) {
        this.responseProperties = responseProperties;
    }

    public TypedProperty getResponseProperties() {
        return this.responseProperties;
    }

    public void performExecute() {
    }
}
