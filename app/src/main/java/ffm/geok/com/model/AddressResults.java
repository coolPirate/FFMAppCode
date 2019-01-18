package ffm.geok.com.model;

public class AddressResults {
    private int layerId;

    private String layerName;

    private String displayFieldName;

    private String value;

    private AddressAttributes attributes;

    public void setLayerId(int layerId){
        this.layerId = layerId;
    }
    public int getLayerId(){
        return this.layerId;
    }
    public void setLayerName(String layerName){
        this.layerName = layerName;
    }
    public String getLayerName(){
        return this.layerName;
    }
    public void setDisplayFieldName(String displayFieldName){
        this.displayFieldName = displayFieldName;
    }
    public String getDisplayFieldName(){
        return this.displayFieldName;
    }
    public void setValue(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }
    public void setAttributes(AddressAttributes attributes){
        this.attributes = attributes;
    }
    public AddressAttributes getAttributes(){
        return this.attributes;
    }
}
