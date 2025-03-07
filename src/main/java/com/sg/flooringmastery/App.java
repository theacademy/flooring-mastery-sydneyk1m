package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.FlooringController;
import com.sg.flooringmastery.dao.FlooringDao;
import com.sg.flooringmastery.dao.FlooringDaoImpl;
import com.sg.flooringmastery.service.FlooringService;
import com.sg.flooringmastery.service.FlooringServiceImpl;
import com.sg.flooringmastery.view.FlooringView;
import com.sg.flooringmastery.view.UserIO;
import com.sg.flooringmastery.view.UserIOImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("com.sg.flooringmastery");
        appContext.refresh();

        FlooringController controller = appContext.getBean("flooringController", FlooringController.class);
        controller.run();
    }
}
