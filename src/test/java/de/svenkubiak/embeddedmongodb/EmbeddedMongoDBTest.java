package de.svenkubiak.embeddedmongodb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.flapdoodle.embed.mongo.distribution.Version;

/**
 * 
 * @author svenkubiak
 *
 */
public class EmbeddedMongoDBTest {
    @Test
    public void testStart() {
        // given
        EmbeddedMongoDB embeddedMongoDB = EmbeddedMongoDB.create().start();
        
        // then
        assertTrue(embeddedMongoDB.isActive());
        embeddedMongoDB.stop();
    }
    
    @Test
    public void testStop() throws InterruptedException {
        // given
        EmbeddedMongoDB embeddedMongoDB = EmbeddedMongoDB.create().start();
        
        // when
        embeddedMongoDB.stop();
        Thread.sleep(5000);
        
        // then
        assertFalse(embeddedMongoDB.isActive());
    }
    
    @Test
    public void testDefaultValues() {
        // given
        EmbeddedMongoDB embeddedMongoDB = EmbeddedMongoDB.create();
        
        // then
        assertFalse(embeddedMongoDB.isActive());
        assertEquals("localhost", embeddedMongoDB.getHost());
        assertEquals(29019, embeddedMongoDB.getPort());
        assertEquals(Version.Main.PRODUCTION, embeddedMongoDB.getVersion());
        embeddedMongoDB.stop();

    }
    
	@Test
	public void testMongoDB() {
	    // given
	    EmbeddedMongoDB embeddedMongoDB = EmbeddedMongoDB.create().start();
	    MongoClient mongoClient = new MongoClient(embeddedMongoDB.getHost(), embeddedMongoDB.getPort());
		
	    // then
	    assertNotNull(mongoClient);
		
	    // given
		MongoDatabase db = mongoClient.getDatabase("embeddedTestDB"); 
		
		// then
		assertNotNull(db);
		
		// given
		MongoCollection<Document> collection = db.getCollection("testCollection");
		
		// then
		assertNotNull(collection);
		
		// when
		for (int i=0; i < 100; i++) {
			collection.insertOne(new Document("i", i));
		}
		
		// then
		assertEquals(100, collection.countDocuments());
        embeddedMongoDB.stop();

	}
}