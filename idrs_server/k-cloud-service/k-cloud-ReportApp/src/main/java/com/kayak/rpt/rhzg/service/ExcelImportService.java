package com.kayak.rpt.rhzg.service;


import java.util.List;
import java.util.Map;

public interface ExcelImportService<T> {

    void importFile(List<T> list, Map map) throws Exception;


}
