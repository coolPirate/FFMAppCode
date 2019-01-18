package ffm.geok.com.model;

import java.util.List;

public class ResponseModelAddress {
    private List<AddressResults> results;

    public void setResults(List<AddressResults> results){
        this.results = results;
    }
    public List<AddressResults> getResults(){
        return this.results;
    }
}
