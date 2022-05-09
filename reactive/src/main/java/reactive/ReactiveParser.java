package reactive;

import java.awt.event.WindowListener;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import reactive.view.ParserFrame;

public class ReactiveParser {
    /* public static void main(String[] args) throws Exception {

		System.out.println("\n=== TEST Hot streams with pubsub ===\n");

		// Subjects: bridges functioning both as observer and observable

		PublishSubject<Integer> source = PublishSubject.<Integer>create();
		 
		log("subscribing.");

		source.subscribe((s) -> {
			log("subscriber A: "+s); 
		}, Throwable::printStackTrace);
		 
		log("generating.");

		new Thread(() -> {
				int i = 0;
				while (i < 100){
					try {
						log("source: "+i); 
						source.onNext(i);
						Thread.sleep(10);
						i++;
					} catch (Exception ex){}
				}
			}).start();
		

		log("waiting.");

		Thread.sleep(100);
		
		source.subscribe((s) -> {
			log("subscriber B: "+s); 
		}, Throwable::printStackTrace);

	} */

    static class MyFrame extends JFrame {	

		private PublishSubject<Integer> stream;
		
		public MyFrame(PublishSubject<Integer> stream){
			super("Swing + RxJava");
			this.stream = stream;
			setSize(150,60);
			setVisible(true);
			JButton button = new JButton("Press me");
			button.addActionListener(e -> {
				stream.onNext(1);
			});
			getContentPane().add(button);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
	}

	static public void main(String[] args){
		
		PublishSubject<Integer> clickStream = PublishSubject.create();
		
		SwingUtilities.invokeLater(()->{
			new MyFrame(clickStream);
		});

		clickStream
			.observeOn(Schedulers.computation())
			.subscribe((v) -> {
				System.out.println(Thread.currentThread().getName() + "click: "+System.currentTimeMillis());
			});

		clickStream
			.buffer(clickStream.throttleWithTimeout(250, TimeUnit.MILLISECONDS))
			.map(xs -> xs.size())
			.filter((v) -> v >= 2)
			.subscribe((v) -> {
				System.out.println(Thread.currentThread().getName() + ": Multi-click: "+v);
			});
		
	}
	
	static private void log(String msg) {
		System.out.println("[" + Thread.currentThread().getName() + "]: " + msg);
	}
}
