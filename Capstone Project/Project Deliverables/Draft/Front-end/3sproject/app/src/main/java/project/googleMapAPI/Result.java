package project.googleMapAPI;

import java.util.List;

public class Result {

    private List<Address_Component> address;

    public Result(List<Address_Component> address_components) {
        this.address = address_components;
    }

    public List<Address_Component> getAddress_components() {
        return address;
    }

    public void setAddress_components(List<Address_Component> address_components) {
        this.address = address_components;
    }
}
