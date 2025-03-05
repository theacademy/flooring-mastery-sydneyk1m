package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.FlooringController;
import com.sg.flooringmastery.dao.FlooringDao;
import com.sg.flooringmastery.dao.FlooringDaoImpl;
import com.sg.flooringmastery.service.FlooringService;
import com.sg.flooringmastery.service.FlooringServiceImpl;
import com.sg.flooringmastery.view.FlooringView;
import com.sg.flooringmastery.view.UserIO;
import com.sg.flooringmastery.view.UserIOImpl;

public class App {
    public static void main(String[] args) {

        UserIO myIo = new UserIOImpl();
        // Instantiate the View and wire the UserIO implementation into it
        FlooringView myView = new FlooringView(myIo);
        // Instantiate the DAO
        FlooringDao myDao = new FlooringDaoImpl();
        // Instantiate the Service Layer and wire the DAO and Audit DAO into it
        FlooringService myService = new FlooringServiceImpl(myDao);
        // Instantiate the Controller and wire the Service Layer into it
        FlooringController controller = new FlooringController(myView, myService);
        // Kick off the Controller
        controller.run();
    }
}
