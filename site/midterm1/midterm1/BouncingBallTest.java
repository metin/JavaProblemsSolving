package midterm1;
import static org.junit.Assert.*;

import org.junit.Test;


public class BouncingBallTest {

	@Test
	public void testPresence() {
		BouncingBall3 bb3 = new BouncingBall3();
		assertFalse(bb3 == null);
	}
	
	@Test
	public void testStrategies() {
		BouncingBall3 bb3 = new BouncingBall3();
		BouncingObjectFactory bof = new BouncingObjectFactory(bb3);
		BouncingObject bobj = bof.makeBouncingObject("circle");
		assertTrue(bobj instanceof BouncingCircle);
		bobj = bof.makeBouncingObject("square");
		assertTrue(bobj instanceof BouncingSquare);
		bobj = bof.makeBouncingObject("image");
		assertTrue(bobj instanceof BouncingImage);
	}

}
