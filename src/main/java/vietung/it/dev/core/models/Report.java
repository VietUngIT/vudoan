package vietung.it.dev.core.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class Report {
    private int count;
    private HashMap<String,ReportObject> lst = new HashMap<>();
}
