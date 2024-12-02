package models;

import io.qameta.allure.internal.shadowed.jackson.databind.node.BooleanNode;

import java.util.List;

public class AllIngredientsResponse {
    private Boolean success;
    private List<DataElement> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<DataElement> getData() {
        return data;
    }

    public void setData(List<DataElement> data) {
        this.data = data;
    }
}
