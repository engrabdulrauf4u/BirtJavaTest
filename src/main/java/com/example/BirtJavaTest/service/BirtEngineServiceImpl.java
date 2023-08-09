package com.example.BirtJavaTest.service;

import com.ibm.icu.util.ULocale;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.*;
import org.eclipse.birt.report.model.api.*;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class BirtEngineServiceImpl implements BirtEngineService{

    @Autowired
    private ApplicationContext context;

    @Autowired
    ServletContext servletContext;

    @Autowired
    private Environment env;

    private static final String LOGS_PATH ="C:temp\\logs" ;

    public BirtEngineServiceImpl() {
        super();
    }

    @Override
    public void saveReport() throws SemanticException, IOException {
           // Create a session handle. This is used to manage all open designs.
            // Your app need create the session only once.

        String filename = "sample.rptdesign";

            //Configure the Engine and start the Platform
            DesignConfig config = new DesignConfig( );

            config.setProperty("BIRT_HOME", "C:/birt-runtime-2_1_1/birt-runtime-2_1_1/ReportEngine");
            IDesignEngine engine = null;
            try{


                Platform.startup( config );
                IDesignEngineFactory factory = (IDesignEngineFactory) Platform
                        .createFactoryObject( IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY );
                engine = factory.createDesignEngine( config );

            }catch( Exception ex){
                ex.printStackTrace();
            }


            SessionHandle session = engine.newSessionHandle( ULocale.ENGLISH ) ;

            // Create a new report design.

            ReportDesignHandle design = session.createDesign( );

            // The element factory creates instances of the various BIRT elements.

            ElementFactory factory = design.getElementFactory( );

            // Create a simple master page that describes how the report will
            // appear when printed.
            //
            // Note: The report will fail to load in the BIRT designer
            // unless you create a master page.

            DesignElementHandle element = factory.newSimpleMasterPage( "Page Master" ); //$NON-NLS-1$
            design.getMasterPages( ).add( element );

            // Create a grid and add it to the "body" slot of the report
            // design.

        GridHandle grid = factory.newGridItem( null, 2 /* cols */, 1 /* row */ );
            design.getBody( ).add( grid );

            // Note: Set the table width to 100% to prevent the label
            // from appearing too narrow in the layout view.

            grid.setWidth( "100%" ); //$NON-NLS-1$

            // Get the first row.

            RowHandle row = (RowHandle) grid.getRows( ).get( 0 );

            // Create an image and add it to the first cell.

            ImageHandle image = factory.newImage( null );
            CellHandle cell = (CellHandle) row.getCells( ).get( 0 );
            cell.getContent( ).add( image );
            image.setURL( "\"http://www.eclipse.org/birt/phoenix/tutorial/basic/multichip-4.jpg\"" );

            // Create a label and add it to the second cell.

            LabelHandle label = factory.newLabel( null );
            cell = (CellHandle) row.getCells( ).get( 1 );
            cell.getContent( ).add( label );
            label.setText( "Hello, world!" ); //$NON-NLS-1$

            // Save the design and close it.

            design.saveAs( "D:/birt/workspace/"+filename); //$NON-NLS-1$
            design.close( );
            System.out.println("Finished");

            // We're done!
        }


    @Override
    public void generate() throws SemanticException, IOException {


        File reportName = getReportFromFilesystem("userprofile");
        IReportEngine engine = null;
        EngineConfig config = null;
        try {
            config = new EngineConfig();


            Platform.startup(config);
            IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
            engine = factory.createReportEngine(config);

            IReportRunnable  design = engine.openReportDesign( reportName.getPath() );


            IRunAndRenderTask task = engine.createRunAndRenderTask(design);
            HTMLRenderOption options = new HTMLRenderOption();
            options.setOutputFileName("userprofile.pdf");
            options.setOutputFormat("pdf");
            task.setRenderOption(options);
            task.run();
            task.close();
            engine.destroy();

        } catch (EngineException e) {
            e.printStackTrace();
        } catch (BirtException e) {
            e.printStackTrace();
        }
    }
    public File getReportFromFilesystem(String reportName) throws RuntimeException {
        String reportDirectory = env.getProperty("birt_report_input_dir");
        Path birtReport = Paths.get(reportDirectory + File.separator + reportName + ".rptdesign");
        if(!Files.isReadable(birtReport))
            throw new RuntimeException("Report " + reportName + " either did not exist or was not writable.");

        return birtReport.toFile();
    }


}
