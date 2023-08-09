package com.example.BirtJavaTest.controller;

import com.example.BirtJavaTest.service.BirtEngineService;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class CreateDesignController {

    @Autowired
    BirtEngineService birtEngineService;

    @GetMapping(value = "/saveDesign")
    public void saveDesign() throws IOException, SemanticException {
        birtEngineService.saveReport();
    }

    @GetMapping(value = "/generateReport")
    public ResponseEntity generateReport() throws IOException, SemanticException {
        try{
            birtEngineService.generate();
        }catch(Exception exception){
            return new ResponseEntity("Not Generated.."+exception.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity("Report Generated..........", HttpStatus.OK);
    }
}
