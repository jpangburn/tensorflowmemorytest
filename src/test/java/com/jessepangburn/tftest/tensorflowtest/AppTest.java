package com.jessepangburn.tftest.tensorflowtest;


import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.tensorflow.DataType;
import org.tensorflow.Graph;
import org.tensorflow.Output;
import org.tensorflow.Session;
import org.tensorflow.Tensor;


/**
 * Unit test for simple App.
 */
public class AppTest 
{
	// a float array to perform work on
	private static final float[] X = new float[]{1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0};

	@Ignore
	@Test
	public void testTensorFlowMemory() {
		// create a graph and session
		try (Graph g = new Graph(); Session s = new Session(g)) {
			// create a placeholder x and a const for the dimension to do a cumulative sum along
			Output x = g.opBuilder("Placeholder", "x").setAttr("dtype", DataType.FLOAT).build().output(0);
			Output dims = g.opBuilder("Const", "dims").setAttr("dtype", DataType.INT32).setAttr("value", Tensor.create(0)).build().output(0);
			Output y = g.opBuilder("Cumsum", "y").addInput(x).addInput(dims).build().output(0);
			// loop a bunch to test memory usage
			for (int i=0; i<10000000; i++){
				// create a tensor from X
				Tensor tx = Tensor.create(X);
				// run the graph and fetch the resulting y tensor
				Tensor ty = s.runner().feed("x", tx).fetch("y").run().get(0);
				// close the tensors to release their resources
				tx.close();
				ty.close();
			}

			System.out.println("non-threaded test finished");
		}
	}
	
	private static final int THREADS = 8;
	
	@Test
	public void testTensorFlowMemoryThreaded() throws Exception {
		// create a graph and session
		try (Graph g = new Graph(); Session s = new Session(g)) {
			Output x = g.opBuilder("Placeholder", "x").setAttr("dtype", DataType.FLOAT).build().output(0);
			Output dims = g.opBuilder("Const", "dims").setAttr("dtype", DataType.INT32).setAttr("value", Tensor.create(0)).build().output(0);
			Output y = g.opBuilder("Cumsum", "y").addInput(x).addInput(dims).build().output(0);
			// make threads to do BusyWork with this graph
			List<Thread> threads = new LinkedList<Thread>();
			for (int i=0; i<THREADS; i++){
				Thread newThread = new Thread(new BusyWork(s), "BusyWork-" + i);
				newThread.start();
				threads.add(newThread);
			}
			// wait for the threads to finish
			for (Thread thread : threads){
				thread.join();
			}
			System.out.println("threaded test finished");
		}
	}
	
	public class BusyWork implements Runnable{

		private Session s;
		public BusyWork(Session s){
			this.s = s;
		}
		
		@Override
		public void run() {
			for (int i=0; i<10000000; i++){
				Tensor tx = Tensor.create(X);
				Tensor ty = this.s.runner().feed("x", tx).fetch("y").run().get(0);
				tx.close();
				ty.close();
			}
			System.out.println("thread finished - " + Thread.currentThread().getName());
		}
		
	}
}
