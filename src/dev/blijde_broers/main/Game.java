package dev.blijde_broers.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

import dev.blijde_broers.input.KeyManager;
import dev.blijde_broers.input.MouseManager;
import dev.blijde_broers.input.MouseWheelManager;
import dev.blijde_broers.misc.math.Math2D;
import dev.blijde_broers.misc.math.Transform;
import dev.blijde_broers.misc.math.Vector2;
import dev.blijde_broers.neuralNetwork.NEAT.Genome;
import dev.blijde_broers.object.GameState;
import dev.blijde_broers.object.Handler;
import dev.blijde_broers.object.components.simulations.instances.PhysicsSimulation;
import dev.blijde_broers.object.instances.Player;
import dev.blijde_broers.object.instances.animals.Animal;
import dev.blijde_broers.object.instances.foods.instances.Pellet;

public class Game implements Runnable {
	private Window window;
	private Thread thread;
	private Handler handler;
	private LoadingScreen loadingScreen;
	private boolean running = false;

	@SuppressWarnings("unused")
	private int fps = 1000;
	private boolean esc;
	private boolean fthree;

	public static boolean debug = true;

	// De huidige status van het spel.
	public static GameState STATE = GameState.Menu;
	public static Game GAME;

	public static int TPS = 60;

	private static final int NUM_OF_FOODS = 150, NUM_OF_CREATURES = 100;
	private static int timeLeftUntilFoodSpawn = 2;

	public static final Transform WORLD_TRANSFORM = new Transform(new Vector2(0, 0), new Vector2(2000, 2000));
	private static final int TIME_BETWEEN_FOOD_SPAWN = 20;

	public Game() {
		start();
	}

	public static void main(String[] args) {
		GAME = new Game();
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() throws InterruptedException {
		window.dispose();
		running = false;
	}

	public void init() {
		window = new Window("New Game", (int) (1680 * 0.7), (int) (1050 * 0.7));
		loadingScreen = new LoadingScreen();
		loadingScreen.start();
		loadingScreen.percentageDone = 0;
		window.getCanvas().addKeyListener(new KeyManager());
		window.getCanvas().addMouseListener(new MouseManager());
		window.getCanvas().addMouseWheelListener(new MouseWheelManager());

		loadingScreen.percentageDone = 33;

		handler = new Handler();
		Handler.simulations.add(new PhysicsSimulation());

		loadingScreen.percentageDone = 67;

		setup();
		System.out.println(Handler.objects.size());

		loadingScreen.percentageDone = 100;
		try {
			loadingScreen.stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setup() {
		handler.removeAll();
		Genome.resetInnovation();
		Handler.objects.add(new Player(new Transform(new Vector2(), new Vector2(100, 100))));
//		Handler.objects.add(new TestObject(new Transform(new Vector2(), new Vector2(100, 100))));
		while(Handler.objects.size() < NUM_OF_CREATURES) {
			Handler.objects.add(new Animal(new Transform(
					Math2D.randomPoint(WORLD_TRANSFORM.getULCorner(), WORLD_TRANSFORM.getLRCorner()).asVector2(),
					new Vector2(40, 40), Math.random() * Math.PI * 2), 100, 1, 1));
		}
	}

	public void tick() {
		if(fps < 20) TPS--;
		if(fps > 1000 && TPS < 60 ) TPS++;
		if(TPS <= 0) TPS = 1;
		handler.tick();
		toggleStates();
		if (KeyManager.pressed[KeyEvent.VK_0])
			TPS++;
		if (KeyManager.pressed[KeyEvent.VK_9])
			TPS--;
		if (Handler.foods.size() < NUM_OF_FOODS) {
			timeLeftUntilFoodSpawn--;
		}
		if(timeLeftUntilFoodSpawn <= 0) {
			Handler.foods.add(new Pellet(
					Math2D.randomPoint(WORLD_TRANSFORM.getULCorner(), WORLD_TRANSFORM.getLRCorner()).asVector2()));
			timeLeftUntilFoodSpawn = TIME_BETWEEN_FOOD_SPAWN;
		}
//		if (Handler.objects.size() < NUM_OF_CREATURES) {
//			Handler.objects.add(new Animal(new Transform(
//					Math2D.randomPoint(WORLD_TRANSFORM.getULCorner(), WORLD_TRANSFORM.getLRCorner()).asVector2(),
//					new Vector2(40, 40), Math.random() * Math.PI * 2), 100, 1, 1));
//		}
	}

	public void render() {
		BufferStrategy bs = window.getCanvas().getBufferStrategy();
		if (bs == null) {
			window.getCanvas().createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		g.setColor(Color.gray);
		g.fillRect(0, 0, window.getWidth(), window.getHeight());
		g.setColor(Color.white);

		handler.render(g);

		g.setColor(Color.white);
		if (debug) {
			g.drawString(Double.toString(Handler.mainCamera.zoom), 10, 20);
			g.drawString(Integer.toString(TPS), 10, 40);
			g.drawString(Integer.toString(Handler.objects.size()), 10, 60);
			g.drawString(Integer.toString(Genome.getInnovation()), 10, 80);
		}
		g.dispose();
		bs.show();
	}

	public void run() {
		init();
		int frames = 0;
		long lastTime = System.nanoTime();
		double ns = 1000000000 / TPS;
		double delta = 0;
		long timer = System.currentTimeMillis();

		LinkedList<Long> renderTime = new LinkedList<Long>();
		LinkedList<Long> tickTime = new LinkedList<Long>();
		while (running) {
			ns = 1000000000 / TPS;
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				long start = System.nanoTime();
				tick();
				tickTime.add((System.nanoTime() - start));
				delta--;
			}
			if (running) {
				frames++;
			}
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				fps = frames;
				frames = 0;
				long average = 0;
				for (long l : tickTime) {
					average += l;
				}
				if (tickTime.size() > 0)
					System.out.println("Tick: " + (average / tickTime.size()) / 1000);
				average = 0;
				for (long l : renderTime) {
					average += l;
				}
				if (renderTime.size() > 0)
					System.out.println("Render: " + (average / renderTime.size()) / 1000);
				while (tickTime.size() > 0) {
					tickTime.removeFirst();
				}
				while (renderTime.size() > 0) {
					renderTime.removeFirst();
				}
			}
			if (running) {
				long start = System.nanoTime();
				render();
				renderTime.add((System.nanoTime() - start));
			}
		}
	}

	public void toggleStates() {
		boolean temp = esc;
		esc = false;
		if (KeyManager.pressed[KeyEvent.VK_ESCAPE]) {
			esc = true;
		}
		if (esc == true && temp == false) {
			if (STATE == GameState.Game) {
				STATE = GameState.Menu;
			} else {
				// STATE = GameState.Game;
			}
		}
		temp = fthree;
		fthree = false;
		if (KeyManager.pressed[KeyEvent.VK_F3]) {
			fthree = true;
		}
		if (fthree == true && temp == false) {
			if (debug) {
				debug = false;
			} else {
				debug = true;
			}
		}
		if (KeyManager.pressed[KeyEvent.VK_R]) {
			setup();
		}
	}

	public Window getWindow() {
		return window;
	}

	public void setWindow(Window window) {
		this.window = window;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}
