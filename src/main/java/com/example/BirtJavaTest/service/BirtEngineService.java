package com.example.BirtJavaTest.service;

import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface BirtEngineService {

    void saveReport() throws SemanticException, IOException;
    void  generate() throws SemanticException, IOException;

}
