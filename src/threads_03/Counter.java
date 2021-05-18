package threads_03;

/**
 *
 * @author rnavarro
 */
public class Counter {
    
     protected long count = 0;
     
     public Counter() {
         this.count = 0;
     }
     
     public Counter(long n) {
         this.count = n;
     }

     public synchronized void add(long value){
         this.count = this.count + value;
     }
     
     public long get() {
         return this.count;
     }
    
}
