package learn;

import java.io.File;
import java.util.UUID;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

// http://docs.oracle.com/cd/E17277_02/html/GettingStartedGuide/index.html
public class LearnBdb {
	
	
	private static class DeliveryRequest {
		
	}	
	
	private static class CaptureDao {
		
		public void put() {
						
		}
		
		public void get() {
			
		}
		
		public void delete() {
			
		}
		
	}
	
	@Entity
	private static class EntityA {
		
		@PrimaryKey
		private String primaryKey;
				
		private String value;
	
		
		public String getPrimaryKey() {
			return primaryKey;
		}
		
		public void setPrimaryKey(String primaryKey) {
			this.primaryKey = primaryKey;
		}
		
		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {		
			return super.toString() + " " + primaryKey + " " + value;
		}
		
	}
	
	@Entity
	private static class EntityB {
		
		@PrimaryKey
		private String primaryKey;
				
		private Integer value;
		
		public void setPrimaryKey(String primaryKey) {
			this.primaryKey = primaryKey;
		}
		
		public Integer getValue() {
			return value;
		}
		
		public void setValue(Integer value) {
			this.value = value;
		}
		
		@Override
		public String toString() {		
			return super.toString() + " " + primaryKey + " " + value;
		}
		
	}

	public static void main(String[] args) {
		
		Environment myEnv = null;
		EntityStore store = null;
		
		try {
			
		    EnvironmentConfig myEnvConfig = new EnvironmentConfig();
		    StoreConfig storeConfig = new StoreConfig();

		    myEnvConfig.setAllowCreate(true);
		    storeConfig.setAllowCreate(true);

		    File home = new File("./bdb-home");
			// Open the environment and entity store
			myEnv = new Environment(home, myEnvConfig);
			store = new EntityStore(myEnv, "EntityStore", storeConfig);
		    
			
			PrimaryIndex<String, EntityA> primaryKeyA =
					store.getPrimaryIndex(String.class, EntityA.class);
			
			PrimaryIndex<String, EntityB> primaryKeyB =
					store.getPrimaryIndex(String.class, EntityB.class);
		    
			UUID id1 = UUID.randomUUID();
			EntityA entityA = new EntityA();			
			entityA.setPrimaryKey(id1.toString());
			entityA.setValue("value1");
			
			UUID id2 = UUID.randomUUID();
			EntityB entityB = new EntityB();			
			entityB.setPrimaryKey(id2.toString());
			entityB.setValue(1);
			
			primaryKeyA.put(entityA);			
			primaryKeyB.put(entityB);
			
			EntityA put = primaryKeyA.put(entityA);
			System.out.println(put);
			
			primaryKeyB.put(entityB);
			
			System.out.println(entityA);
			System.out.println(entityB);
			
			{ 
				EntityA reloadedA = primaryKeyA.get(id1.toString());
				EntityB reloadedB = primaryKeyB.get(id2.toString());
				
				System.out.println(reloadedA);
				System.out.println(reloadedB);
			}
			
			{
				primaryKeyA.delete(id1.toString());
				primaryKeyB.delete(id2.toString());
				
				EntityA reloadedA = primaryKeyA.get(id1.toString());
				EntityB reloadedB = primaryKeyB.get(id2.toString());
				
				System.out.println(reloadedA);
				System.out.println(reloadedB);
			}
		    
		    
		} catch(DatabaseException dbe) {
		    System.err.println("Error opening environment and store: " +
		                        dbe.toString());
		    System.exit(-1);
		} 
		
		if (store != null) {
		    try {
		        store.close();
		    } catch(DatabaseException dbe) {
		        System.err.println("Error closing store: " +
		                            dbe.toString());
		        System.exit(-1);
		    }
		}

		if (myEnv != null) {
		    try {
		        // Finally, close environment.
		        myEnv.close();
		    } catch(DatabaseException dbe) {
		        System.err.println("Error closing MyDbEnv: " +
		                            dbe.toString());
		        System.exit(-1);
		    }
		} 
		
	}
	
}
