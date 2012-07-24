package net.caprazzi.tools.sbatti.io.bdb;

import java.io.File;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;

public class BdbCaptureEnvironment {
	
	private Environment storeEnv;
	private EntityStore entityStore;

	// The setup() method opens the environment and store
    // for us.
    public BdbCaptureEnvironment(File envHome)
        throws DatabaseException {

        EnvironmentConfig myEnvConfig = new EnvironmentConfig();
        StoreConfig storeConfig = new StoreConfig();     

        // Create the environment and entity store if 
        // they do not exist.
        myEnvConfig.setAllowCreate(true);
        storeConfig.setAllowCreate(true);
        
        myEnvConfig.setTransactional(true);
        storeConfig.setTransactional(true);

        // Open the environment and entity store
        storeEnv = new Environment(envHome, myEnvConfig);
        entityStore = new EntityStore(storeEnv, "EntityStore", storeConfig);

    }
    
    public EntityStore getEntityStore() {
		return entityStore;
	}
    
    
    public void close() {
        if (storeEnv != null) {
            try {
            	storeEnv.close();
            } catch(DatabaseException dbe) {
                System.err.println("Error closing store: " +
                                    dbe.toString());
               System.exit(-1);
            }
        }

        if (storeEnv != null) {
            try {
                // Finally, close the environment.
            	storeEnv.close();
            } catch(DatabaseException dbe) {
                System.err.println("Error closing MyDbEnv: " +
                                    dbe.toString());
               System.exit(-1);
            }
        }
    }
    
	
}
